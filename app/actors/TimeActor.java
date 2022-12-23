package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import scala.concurrent.duration.Duration;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TimeActor extends AbstractActorWithTimers {

	Set<ActorRef> userActors = new HashSet<>();

	public static class RegisterMsg {
	}

	private static final class Tick {
	}

	public static Props props() {
		return Props.create(TimeActor.class,() -> new TimeActor());
	}

	public void preStart() {
		getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(5, TimeUnit.SECONDS));
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Tick.class, msg -> notifyClients())
				.match(RegisterMsg.class, msg -> userActors.add(sender()))
				.build();
	}
	private void notifyClients(){
		UserActor.TimeMessage tMsg = new UserActor.TimeMessage(LocalDateTime.now().toString());
		userActors.forEach(ar -> ar.tell(tMsg, self()));
	}

}

