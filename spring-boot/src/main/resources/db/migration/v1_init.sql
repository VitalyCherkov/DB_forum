CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;


-- ######################################################################
--
-- User
--
-- ######################################################################

CREATE TABLE "User"
(
  id BIGSERIAL
    CONSTRAINT User_id_pk PRIMARY KEY,

  email CITEXT NOT NULL
    CONSTRAINT User_email_unique UNIQUE,

  nickname CITEXT COLLATE ucs_basic NOT NULL
    CONSTRAINT User_nickname_unique UNIQUE,

  fullname TEXT,

  about TEXT
);

-------------------------------------------------------------------------
-- User indexes
-------------------------------------------------------------------------

-- В названии ui - unique index
CREATE UNIQUE INDEX User_ui_id
  ON "User" (id);

CREATE UNIQUE INDEX User_ui_nickname
  ON "User" (nickname);

CREATE UNIQUE INDEX User_ui_email
  ON "User" (email);


-- ######################################################################
--
-- Forum
--
-- ######################################################################

CREATE TABLE Forum
(
  id BIGSERIAL
    CONSTRAINT Forum_id_pk PRIMARY KEY,

  userid BIGINT NOT NULL
    REFERENCES "User" (id),

  slug CITEXT NOT NULL,

  posts INTEGER DEFAULT 0 NOT NULL,

  threads INTEGER DEFAULT 0 NOT NULL,

  TITLE TEXT
);

-------------------------------------------------------------------------
-- Forum indexes
-------------------------------------------------------------------------

CREATE UNIQUE INDEX Forum_ui_id
  ON Forum (id);

CREATE UNIQUE INDEX Forum_ui_slug
  ON Forum (slug);

-- ######################################################################
--
-- Thread
--
-- ######################################################################

CREATE TABLE Thread
(
  id BIGSERIAL
    CONSTRAINT Thread_id_pk PRIMARY KEY,

  userid BIGINT NOT NULL
    REFERENCES "User" (id),

  forumid BIGINT NOT NULL
    REFERENCES Forum (id),

  slug CITEXT,

  title TEXT,

  messagetext TEXT NOT NULL,

  created TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,

  votes INTEGER DEFAULT 0 NOT NULL
);

-------------------------------------------------------------------------
-- Thread indexes
-------------------------------------------------------------------------

CREATE UNIQUE INDEX Thread_ui_id
  ON Thread (id);

CREATE UNIQUE INDEX Thread_ui_slug
  ON Thread (slug);

-- TODO: Thread indexes for acs & desc by created


-- ######################################################################
--
-- Messages
--
-- ######################################################################

CREATE TABLE Messages
(
  id BIGSERIAL
    CONSTRAINT Messages_id_pk PRIMARY KEY,

  userid BIGINT NOT NULL
    REFERENCES "User" (id),

  threadid BIGINT NOT NULL
    REFERENCES Thread (id),

  message TEXT,

  parentid BIGINT DEFAULT 0,

  created TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,

  isedited BOOLEAN DEFAULT FALSE NOT NULL,

  path INTEGER []
);

-------------------------------------------------------------------------
-- Messages indexes
-------------------------------------------------------------------------

CREATE UNIQUE INDEX Messages_ui_id
  ON Messages (id);

CREATE INDEX Messages_i_asc_parentid_threadid
  ON Messages (parentid, threadid, path, id);

CREATE INDEX Messages_i_desc_parentid_threadid
  ON Messages (parentid, threadid, path DESC, id DESC);

CREATE INDEX Messages_i_asc_threadid
  ON Messages (threadid, path, id);

CREATE INDEX Messages_i_desc_parentid_threadid
  ON Messages (threadid, path DESC, id DESC);

CREATE INDEX Messages_i_root
  ON Messages (threadid, path, id)
  WHERE threadid is 0;

CREATE INDEX Messages_i_asc_parentid_created
  ON Messages (threadid, id);

CREATE INDEX Messages_i_desc_parentid_created
  ON Messages (threadid, id DESC);

-- TODO: Message first parent indexes


-- ######################################################################
--
-- Votes many to many
--
-- ######################################################################

-------------------------------------------------------------------------
--
-------------------------------------------------------------------------

CREATE TABLE Votes
(
  threadid BIGINT NOT NULL
    REFERENCES Thread (id),

  nickname CITEXT NOT NULL
    REFERENCES "User" (nickname),

  voice INTEGER NOT NULL DEFAULT 0,

  CONSTRAINT Votes_unique
    UNIQUE (threadid, nickname)
);

-------------------------------------------------------------------------
-- Votes indexes
-------------------------------------------------------------------------


-- ######################################################################
--
-- ForumUser many to many
--
-- ######################################################################

CREATE TABLE ForumUser
(
  id BIGSERIAL
    CONSTRAINT ForumUser_id_pk PRIMARY KEY,

  userid BIGINT,

  nickname CITEXT COLLATE usc_basic,

  forumslug CITEXT,

  CONSTRAINT ForumUser_unique
    UNIQUE (forumslug, nickname)
);

-------------------------------------------------------------------------
-- ForumUser indexes
-------------------------------------------------------------------------

CREATE INDEX ForumUser_i_acs_forumslug_nickname
  ON ForumUser (forumslug, nickname);

CREATE INDEX ForumUser_i_desc_forumslug_nickname
  ON ForumUser (forumslug, nickname DESC);


-- ######################################################################
--
-- TRIGGERS
-- TODO: triggers and functions
-- Inners start with 'T: '
--
-- ######################################################################

-------------------------------------------------------------------------
-- T: Forum threads inc
-------------------------------------------------------------------------

CREATE FUNCTION inc_forum_threads()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN

  UPDATE Forum
  SET threads = Forum.threads + 1
  WHERE id = NEW.forumid;
  RETURN NEW;

END;
$$;

CREATE TRIGGER _tr_inc_forum_threads
  AFTER INSERT
  ON Thread
  FOR EACH ROW
  EXECUTE PROCEDURE inc_forum_threads();

-------------------------------------------------------------------------
-- T: Vote add
-------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION do_vote()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS $$
BEGIN

  UPDATE Thread
    SET votes = votes + NEW.voice
    WHERE id = NEW.threadid;
  RETURN NEW;

END;
$$;

CREATE TRIGGER _tr_do_vote
  AFTER INSERT
  ON Votes
  FOR EACH ROW
  EXECUTE PROCEDURE do_vote();

-------------------------------------------------------------------------
-- T: Vote change
-------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION change_vote()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS $$
BEGIN

  UPDATE Thread
    SET votes = votes - OLD.voice + NEW.voice
    WHERE id = OLD.threadid;
  RETURN NEW;

END;
$$;

CREATE TRIGGER _tr_change_vote
  AFTER UPDATE
  ON Votes
  FOR EACH ROW
  EXECUTE PROCEDURE change_vote();

-------------------------------------------------------------------------
-- T: Forum posts inc
-------------------------------------------------------------------------


