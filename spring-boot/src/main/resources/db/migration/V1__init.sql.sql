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
CREATE UNIQUE INDEX IF NOT EXISTS User_ui_id
  ON "User" (id);

CREATE UNIQUE INDEX IF NOT EXISTS User_ui_nickname
  ON "User" (nickname);

CREATE UNIQUE INDEX IF NOT EXISTS User_ui_email
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

  title TEXT
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

  forumslug CITEXT,

  nickname CITEXT,

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
-- Post
--
-- ######################################################################

CREATE TABLE Post
(
  id BIGSERIAL
    CONSTRAINT Post_id_pk PRIMARY KEY,

  userid BIGINT NOT NULL
    REFERENCES "User" (id),

  threadid BIGINT NOT NULL
    REFERENCES Thread (id),

  message TEXT,

  parentid BIGINT DEFAULT 0,

  created TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,

  isedited BOOLEAN DEFAULT FALSE NOT NULL,

  path INTEGER [],

  threadslug CITEXT,

  forumslug CITEXT
);

-------------------------------------------------------------------------
-- Post indexes
-------------------------------------------------------------------------

CREATE UNIQUE INDEX IF NOT EXISTS Post_ui_id
  ON Post (id);

CREATE INDEX IF NOT EXISTS Post_i_asc_parentid_threadid
  ON Post (parentid, threadid, path, id);

CREATE INDEX IF NOT EXISTS Post_i_desc_parentid_threadid
  ON Post (parentid, threadid, path DESC, id DESC);

CREATE INDEX IF NOT EXISTS Post_i_asc_threadid
  ON Post (threadid, path, id);

CREATE INDEX IF NOT EXISTS Post_i_desc_threadid
  ON Post (threadid, path DESC, id DESC);

CREATE INDEX IF NOT EXISTS Post_i_root
  ON Post (threadid, path, id)
  WHERE threadid = 0;

CREATE INDEX IF NOT EXISTS Post_i_asc_parentid_created
  ON Post (threadid, id);

CREATE INDEX IF NOT EXISTS Post_i_desc_parentid_created
  ON Post (threadid, id DESC);

-- TODO: Message first parentId indexes


-- ######################################################################
--
-- Votes many to many
--
-- ######################################################################

-------------------------------------------------------------------------
--
-------------------------------------------------------------------------

CREATE TABLE Vote
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

  nickname CITEXT COLLATE ucs_basic,

  forumslug CITEXT,

  CONSTRAINT ForumUser_unique
    UNIQUE (forumslug, nickname)
);

-------------------------------------------------------------------------
-- ForumUser indexes
-------------------------------------------------------------------------

CREATE INDEX IF NOT EXISTS ForumUser_i_acs_forumslug_nickname
  ON ForumUser (forumslug, nickname);

CREATE INDEX IF NOT EXISTS ForumUser_i_desc_forumslug_nickname
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
  ON Vote
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
  ON Vote
  FOR EACH ROW
  EXECUTE PROCEDURE change_vote();

-------------------------------------------------------------------------
-- T: Forum posts inc
-------------------------------------------------------------------------


