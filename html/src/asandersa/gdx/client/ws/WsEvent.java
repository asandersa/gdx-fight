package asandersa.gdx.client.ws;

import com.google.gwt.core.client.JavaScriptObject;


public class WsEvent extends JavaScriptObject {
    protected WsEvent() {
    }

    public final native String getData()  //native - метод без тела
            /*-{
                return this.data; //создаем обертку объекта и возвращаем его поле data
            }-*/
    ;
}
