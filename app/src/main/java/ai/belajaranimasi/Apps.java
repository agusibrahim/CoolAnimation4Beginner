package ai.belajaranimasi;
import android.app.*;

public class Apps extends Application
{
	@Override
	public void onCreate() {
		FontsOverride.setDefaultFont(this, "SERIF","fonts/Montserrat-Regular.otf");
		super.onCreate();
	}
}
