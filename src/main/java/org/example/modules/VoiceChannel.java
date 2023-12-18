package org.example.modules;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class VoiceChannel implements Serializable {
	@Serial
	private static final long serialVersionUID = 99808453L;
	public Long id;
	public String name;
	public Long serverId;
	public Long sequence;
	public Timestamp createdAt;
	public VoiceChannel() {

	}

	@Override
	public String toString() {
		return "VoiceChannel:\n" +
				"id: " + id.toString() + "\n" +
				"serverId: " + serverId.toString() + "\n" +
				"sequence: " + sequence.toString() + "\n" +
				"createdAt: " + createdAt.toString() + "\n";
	}
}