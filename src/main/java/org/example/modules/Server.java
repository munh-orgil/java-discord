package org.example.modules;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Server implements Serializable {
	@Serial
	private static final long serialVersionUID = 99808453L;
	public Long id;
	public String name;
	public File logo;
	public User owner;
	public Timestamp createdAt;
	public List<TextChannel> textChannels;
	public List<VoiceChannel> voiceChannels;

	public Server() {}

	@Override
	public String toString() {
		return "Server:\n" +
				"id: " + id.toString() + "\n" +
				"name: " + name + "\n" +
				"owner: " + owner.nickname + "\n" +
				"createdAt: " + createdAt.toString() + "\n";
	}
}