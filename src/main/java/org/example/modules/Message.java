package org.example.modules;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
	@Serial
	private static final long serialVersionUID = 99808453L;
	public Long id;
	public String content;
	public User author;
	public TextChannel textChannel;
	public File attachment;
	public Timestamp createdAt;
	public Message() {}

	@Override
	public String toString() {
		return "Message:\n" +
				"id: " + id.toString() + "\n" +
				"content: " + content + "\n" +
				"authorId: " + author.nickname + "\n" +
				"textChannel: " + textChannel.name + "\n" +
				"createdAt: " + createdAt.toString() + "\n";
	}
}