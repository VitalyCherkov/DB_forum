-- -- user create
-- INSERT INTO "Users" (email::CITEXT, nickname::CITEXT, fullname, about) VALUES
--     (?, ?, ?, ?);

-- -- get user
-- -- nickname второй раз не забираем, потому что уже ведь он есть
-- SELECT about, email, fullname FROM "Users"
--     WHERE nickname = ?::CITEXT;

-- -- update user
-- UPDATE "Users" 
--     SET (about, email, fullname) = (
--         COALESCE(?, about),
--         COALESCE(?::CITEXT, email), 
--         COALESCE(?, fullname)
--     )
--     WHERE "Users".nickname = ?::CITEXT;

-- -- create forum
-- INSERT INTO Forum (slug, title, userid)
--     SELECT ?, ?, id FROM "Users"
--         WHERE "Users".nickname = ?::CITEXT
--     RETURNING posts, slug, threads, title, user;

--  create thread ... 
-- INSERT INTO Thread (forumid, slug, messagetext, title, created, userid)
--     SELECT ?, ?, ?, id FROM "Users"
--         WHERE "Users".nickname = ?::CITEXT
--     RETURNING *;

-- -- на вход author, created, message, title
-- INSERT INTO Thread (userid, nickname, forumid, forumslug, messagetext, slug, title, created)
-- SELECT
--     U.id, U.nickname, F.id, F.slug::CITEXT, ?, ?, ?, ?::TIMESTAMPTZ
--     FROM Forum F, (
--         SELECT id, nickname FROM "User"
--         WHERE "USER".nickname = ?::CITEXT
--         ) U,
--         WHERE F.slug = ?::CITEXT RETURNING *;



-- -- Forum details
-- -- слаг уже есть, поэтому не забираем его
-- SELECT posts, threads, title, nickname FROM
--     (
--         SELECT posts, threads, title, userid FROM Forum
--             WHERE Forum.slug = ?::CITEXT
--     ) AS F
--     JOIN (
--         SELECT nickname, id AS uid FROM "Users"
--     ) AS U
--         ON U.id = F.userid;

-- -- Forum threads
-- SELECT T.nickname AS author, 
--         T.created AS created,
--         T.id AS id,
--         T.forumslug AS forum,
--         T.messagetext AS "message",
--         T.slug AS slug,
--         T.title AS title,
--         T.votes AS votes
--     FROM Thread T
--     WHERE ?::CITEXT = T.forumslug AND T.created >= ?::TIMESTAMPTZ
--     ORDER BY T.created ASC -- DESC
--     LIMIT ?;

-- Forum users
-- SELECT 
--     U.about AS about, 
--     U.email AS email, 
--     U.fullname AS fullname, 
--     U.nickname AS nickname
--     FROM (
--         SELECT ForumUser.userid AS fid FROM ForumUser
--             WHERE ForumUser.forumslug = ?::CITEXT
--                 AND ForumUser.nickname > ?::CITEXT
--     ) AS F
--     JOIN User AS U ON U.id = F.userid
--     ORDER BY F.nickname ASC -- DESC
--     LIMIT ?;

-- -- SELECT Post details
-- SELECT * FROM Post
--     WHERE Post.id = ?;

-- -- UPDATE Post details
-- UPDATE Post
--     SET Post.messagetext = ?
--     WHERE Post.id = id
--     RETURNING *;

-- Service clear
-- TODO: Service status

-- SELECT COUNT(*) AS forum, SUM(threads) AS thread, SUM(posts) AS post
--     FROM Forum F;

-------------------------------------------------------------------

-- Thread create posts
-- by slug
-- by id

-- slug_or_id
-- posts [
--     author,
--     message,
--     parent
-- ]

-- Post содержит:
--   id BIGSERIAL
--   userid BIGINT NOT NULL
--   threadid BIGINT NOT NULL
--   messagetext TEXT,
--   parentid BIGINT DEFAULT 0,
--   created TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
--   isedited BOOLEAN DEFAULT FALSE NOT NULL,
--   path INTEGER [],
--   usernickname CITEXT,
--   threadslug CITEXT,
--   forumslug CITEXT
-- );

-- Получить threadslug и threadid

-- SELECT id, slug, forumid, forumslug FROM Thread
--     WHERE id = ?;
--     -- WHERE slug = ?::CITEXT;

-- INSERT INTO Post (userid, usernickname, threadid, threadslug, forumid, forumslug, messagetext, created, parentid)
--     SELECT U.id, U.nickname, ?, ?::CITEXT, ?, ?::CITEXT, ?, ?, ?
--     FROM User AS U
--     WHERE U.nickname = ?::CITEXT;

-- -- Update number of posts
-- UPDATE Forum
--     SET posts = posts + ?
--     WHERE id = ?;



-------------------------------------------------------------------

-- -- Thread details 
-- SELECT 
--     T.id AS id,
--     T.slug AS slug,
--     T.messagetext AS "message",
--     T.nickname AS author,
--     T.created AS created,
--     T.forumslug AS forum,
--     T.title AS title,
--     T.votes AS votes
-- FROM Thread 
--     WHERE T.id = ?;
--     -- WHERE T.slug = ?::CITEXT;

-- -- Update thread
-- UPDATE Thread T
--     SET (T.messagetext, T.title) = (?, ?)
--     WHERE T.id = ?
--     RETURNING 
--         T.id AS id,
--         T.slug AS slug,
--         T.messagetext AS "message",
--         T.nickname AS author,
--         T.created AS created,
--         T.forumslug AS forum,
--         T.title AS title,
--         T.votes AS votes;
--     -- WHERE T.slug = ?::CITEXT;

-- TODO: Posts by thread

-- Flat sort
SELECT * FROM Post
    WHERE threadid = ?
        AND id < since -- if DESC
        -- AND id > since -- id ASC
        ORDER BY id DESC -- ASC
        LIMIT ?;

-- Tree sort
SELECT * FROM Post
    WHERE threadid = ?
        AND path > (SELECT path FROM Posts WHERE id = ?) -- < if DESC
    ORDER BY path ASC, id ASC -- or DESC
    LIMIT ?;

-- P Tree Sort
SELECT * FROM Post
WHERE path[1] IN (
    SELECT id FROM Post
    WHERE threadid = ? AND parentid = 0
        AND path < (SELECT path FROM messages WHERE id = ?)
        LIMIT ?
)
ORDER BY path DESC, id DESC;


-- do Vote

-- INSERT INTO Vote (nickname, voice, threadid)
--     SELECT ?::CITEXT, ?, T.id FROM Thread T
--         WHERE T.slug = ?::CITEXT
--     ON CONFLICT (nickname, threadid) 
--     DO UPDATE
--         SET voice = ?;


-- INSERT INTO Vote (nickname, voice, threadid)
--     VALUES(?::CITEXT, ?, ?) 
--     ON CONFLICT (nickname, threadid)
--     DO UPDATE
--         SET voice = ?;

-- SELECT * FROM thread WHERE id = ?;


    




    






