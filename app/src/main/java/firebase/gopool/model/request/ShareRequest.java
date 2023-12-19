package firebase.gopool.model.request;

import java.io.Serializable;

public class ShareRequest implements Serializable {

    private boolean typeShare;
    private int routeId;

    public ShareRequest(boolean typeShare, int routeId) {
        this.typeShare = typeShare;
        this.routeId = routeId;
    }

    public boolean isTypeShare() {
        return typeShare;
    }

    public void setTypeShare(boolean typeShare) {
        this.typeShare = typeShare;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
