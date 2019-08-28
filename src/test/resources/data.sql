INSERT INTO user(name, email, password, active)
VALUES ('회원에이', 'a@test.com', '1234qwer', true);

INSERT INTO user(name, email, password, active)
VALUES ('회원비', 'b@test.com', '1234qwer', true);

INSERT INTO user(name, email, password, active)
VALUES ('회원씨', 'c@test.com', '1234qwer', true);

INSERT INTO user(name, email, password, active)
VALUES ('회원디', 'd@test.com', '1234qwer', true);

INSERT INTO video (create_time, update_time, title, description, content_path, thumbnail_path, origin_file_name, views, writer_id)
VALUES ('2018-08-27 15:15:29.835', '2019-08-27 15:15:29.835', 'video_1', '비디오 1 입니다.', 'pathA', 'thumbnailA', 'video_a.mp4', 10, 1);

INSERT INTO video (create_time, update_time, title, description, content_path, thumbnail_path, origin_file_name, views, writer_id)
VALUES ('2018-08-27 15:16:29.835', '2019-08-27 15:16:29.835', 'video_2', '비디오 2 입니다.', 'pathB', 'thumbnailB', 'video_b.mp4', 100, 1);

INSERT INTO video (create_time, update_time, title, description, content_path, thumbnail_path, origin_file_name, views, writer_id)
VALUES ('2018-11-27 15:17:29.835', '2019-08-27 15:17:29.835', 'video_3', '비디오 3 입니다.', 'pathC', 'thumbnailC', 'video_c.mp4', 1000, 1);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 A의 회원 A의 댓글', 1, 1);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 A의 회원 B의 댓글', 1, 2);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 B의 회원 A의 댓글', 2, 1);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 B의 회원 B의 댓글', 2, 2);

INSERT INTO video_like (like_user_id, video_id)
VALUES (1, 2);

INSERT INTO video_like (like_user_id, video_id)
VALUES (1, 3);

INSERT INTO video_like (like_user_id, video_id)
VALUES (2, 3);

INSERT INTO subscription (subscriber_id, subscribed_user_id)
VALUES (1, 2);

INSERT INTO subscription (subscriber_id, subscribed_user_id)
VALUES (4, 1);
