package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public final class UserActor extends AbstractActor {
    private final ActorRef ws;

    public UserActor(ActorRef wsOut) {
        ws =  wsOut;
    }

    public static Props props(ActorRef wsout) {
        return Props.create(UserActor.class, wsout);
    }

    @Override
    public void preStart() {
        System.out.println("user actor started 3");
        context().actorSelection("/user/timeActor")
                .tell(new TimeActor.RegisterMsg(), self());

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(TimeMessage.class, this::sendTime).build();
    }

    static public class TimeMessage {
        public final String time;
        public TimeMessage(String time) {
            this.time = time;
        }
    }

    private void sendTime(TimeMessage msg) {
        System.out.println("sending message");
        final ObjectNode response = Json.newObject();
        response.put("time", msg.time);
        ws.tell(response, self());
    }
}