INSERT
  INTO users
      (username, password_hash, enabled, role)
VALUES
    ('koki', '$2a$10$QD5gM9Q123bpCPCLdixGR.irkCqEAJf9kilDU1nIvs4zOfbJrKBiq', TRUE, 'USER'),
    ('lusia', '$2a$10$iYawSCstahFtzyvb.wuhMew6k4SROvy.KfoJkP35ZoDJLBgey44Pe', TRUE, 'USER'),
    ('pepe', '$2a$10$wxPD4oSJTf.IdSWFoJ7CKezJgQnVO/YRocv7Br9trIh34IYV.D2bK', TRUE, 'USER');

-- INSERT
--   INTO friend_requests
--       (requester_id, recipient_id, status, created_at)
-- VALUES
--     (2, 1, 'PENDING', NOW());

INSERT
  INTO friends
      (user_id1, user_id2)
VALUES
    (1, 3),
    (3, 2);