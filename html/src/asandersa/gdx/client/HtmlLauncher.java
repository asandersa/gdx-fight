package asandersa.gdx.client;

import asandersa.gdx.client.dto.InputStateImpl;
import asandersa.gdx.client.ws.EventListenerCallback;
import asandersa.gdx.client.ws.WebSocket;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import asandersa.gdx.Starter;
import com.google.gwt.user.client.Timer;

public class HtmlLauncher extends GwtApplication {
        private MessageProcessor messageProcessor;

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        private native WebSocket getWebSocket(String url)
                /*-{
                        return new WebSocket(url);
                }-*/
        ;

        private native void log(Object obj) //будет слать логи
                /*-{
                        console.log(obj);
                }-*/
        ;


        private native String toJson(Object obj)
                /*-{
                        return JSON.stringify(obj);
                }-*/
        ;


        @Override
        public ApplicationListener createApplicationListener () {
                WebSocket client = getWebSocket("ws://localhost:8888/ws");

                Starter starter = new Starter(new InputStateImpl());
                messageProcessor = new MessageProcessor(starter);

                starter.setMessageSender(message -> {
                        client.send(toJson(message));
                });

                Timer timer = new Timer() {

                        @Override
                        public void run() {
                                starter.handleTimer();
                        }
                };

                EventListenerCallback callback = event -> {
                        messageProcessor.processEvent(event);
                };
                client.addEventListener("open", event -> {
                        timer.scheduleRepeating(1000/60);
                        messageProcessor.processEvent(event);
                });
                client.addEventListener("close", callback);
                client.addEventListener("error", callback);
                client.addEventListener("message", callback);
                return starter;
        }
}