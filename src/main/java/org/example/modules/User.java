package org.example.modules;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;


public class User implements Serializable {
	@Serial
	private static final long serialVersionUID = 99808453L;
	public Long id;
	public String email;
	public String nickname;
	public File avatar;
	public String password;
	public String newPassword;
	public Long isMuted;
	public Long isDeafened;
	public Timestamp createdAt;
	public ActionEvent event;

	public User() {}

	@Override
	public String toString() {
		return "User:\n" +
				"id: " + id.toString() + "\n" +
				"email: " + email + "\n" +
				"isMuted: " + isMuted + "\n" +
				"isDeafened: " + isDeafened + "\n" +
				"nickname: " + nickname + "\n";
	}
}