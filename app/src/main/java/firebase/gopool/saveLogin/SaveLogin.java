package firebase.gopool.saveLogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveLogin {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SaveLogin(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public void save(int role, String name, String jwt) {
        editor.putInt("role", role);
        editor.putString("name", name);
        editor.putString("jwt", jwt);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public int getRole() {
        return sp.getInt("role", 0);
    }

    public String getName() {
        return sp.getString("name", "");
    }

    public String getJwt() {
        return sp.getString("jwt", "");
    }

    public String getHeaderJwt() {
        return "Bearer " + getJwt();
    }
}
