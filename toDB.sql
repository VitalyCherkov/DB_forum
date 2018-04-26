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

--  TODO: create thread ... 
INSERT INTO Thread (forumid, slug, messagetext, title, created, userid)
    SELECT ?, ?, ?, id FROM "Users"
        WHERE "Users".nickname = ?::CITEXT
    RETURNING *;

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

-- TODO: Service clear
-- TODO: Service status

-- TODO: Thread create posts
-- by slug
-- by id

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
SELECT 
    P.nickname AS author, 
    P.created AS created,
    P.forumslug AS forum,
    P.id AS id,
    P.isedited AS isEdited,
    P.messagetext AS message,
    P.parent AS parent,
    P.threadid AS thread
FROM Posts P
    WHERE
        P.id > ? AND 
        P.threadslug = ?::CITEXT -- P.id = ?
-- asc / desc
-- order by
-- limit
-- since

-- TODO: do Vote




    




    






