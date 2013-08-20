package actors;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class GameStreamActor extends UntypedActor {
	
	static List<ActorRef> listeners;
	
	static {
		listeners = new LinkedList<ActorRef>();
	}
	
	public enum Command {
		subscribe,
		send
	}
	
	@Override
	public void onReceive(Object msg) throws Exception {
		switch ((Command)msg) {
			case subscribe:
				listeners.add(getSender());
				break;
			case send:
				getSender().tell(new Object(), getSender());
				for (ActorRef actor: listeners) {
					actor.tell("new", getSender());
				}
				System.out.println("Listeners: " + listeners.size());
				listeners.clear();
				break;
			default:
				break;
		}
	}

}
