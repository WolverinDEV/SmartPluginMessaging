package dev.wolveringer.spm.future;

import dev.wolveringer.spm.packets.MessageResponsePacket;

public class MessageStatusResponseFuture extends ObjectProgressFuture<MessageResponsePacket> {
	public MessageStatusResponseFuture() { }
	
	public void done(MessageResponsePacket obj) {
		super.done(obj);
	}
}
