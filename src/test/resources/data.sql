INSERT INTO user(name, email, password, active, profile_image_url, profile_image_file_name)
VALUES ('회원에이', 'a@test.com', '1234qwer', true, 'https://woowa-ioi.s3.ap-northeast-2.amazonaws.com/wootube/basic/default_profile.png', 'default_profile.png');

INSERT INTO user(name, email, password, active, profile_image_url, profile_image_file_name)
VALUES ('회원비', 'b@test.com', '1234qwer', true, 'https://woowa-ioi.s3.ap-northeast-2.amazonaws.com/wootube/basic/default_profile.png', 'default_profile.png');

INSERT INTO user(name, email, password, active, profile_image_url, profile_image_file_name)
VALUES ('회원씨', 'c@test.com', '1234qwer', true, 'https://woowa-ioi.s3.ap-northeast-2.amazonaws.com/wootube/basic/default_profile.png', 'default_profile.png');

INSERT INTO user(name, email, password, active, profile_image_url, profile_image_file_name)
VALUES ('회원디', 'd@test.com', '1234qwer', true, 'https://woowa-ioi.s3.ap-northeast-2.amazonaws.com/wootube/basic/default_profile.png', 'default_profile.png');

INSERT INTO video (title, description, content_path, origin_file_name, writer_id, thumbnail_path, thumbnail_file_name)
VALUES ('video_a', '비디오 에이 입니다.', 'pathA', 'video_a.mp4', 1, 'thumbnailPathA', 'thumbnail_image_a.png');

INSERT INTO video (title, description, content_path, origin_file_name, writer_id, thumbnail_path, thumbnail_file_name)
VALUES ('video_b', '비디오 비 입니다.', 'pathB', 'video_b.mp4', 2, 'thumbnailPathB', 'thumbnail_image_b.png');

INSERT INTO video (title, description, content_path, origin_file_name, writer_id, thumbnail_path, thumbnail_file_name)
VALUES ('video_c', '비디오 씨 입니다.', 'pathC', 'video_c.mp4', 1, 'thumbnailPathC', 'thumbnail_image_c.png');

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 A의 회원 A의 댓글', 1, 1);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 A의 회원 B의 댓글', 1, 2);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 B의 회원 A의 댓글', 2, 1);

INSERT INTO comment (contents, video_id, writer_id)
VALUES ('비디오 B의 회원 B의 댓글', 2, 2);

INSERT INTO subscription (subscriber_id, subscribed_user_id)
VALUES (1, 2);

INSERT INTO subscription (subscriber_id, subscribed_user_id)
VALUES (4, 1);

INSERT INTO video_like (like_user_id, video_id)
VALUES (1, 2);

INSERT INTO video_like (like_user_id, video_id)
VALUES (1, 3);

INSERT INTO video_like (like_user_id, video_id)
VALUES (2, 3);