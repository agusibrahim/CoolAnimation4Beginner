package ai.belajaranimasi;
import android.app.*;
import android.os.*;
import android.view.*;
import android.util.*;
import android.view.animation.*;
import android.animation.*;
import android.view.ViewGroup.*;
import android.widget.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.graphics.drawable.Drawable.*;
import android.media.*;
import android.content.Context;

public class MainActivity extends Activity
{
	View txt_title,txt_desc, btn, btnlabel, gradbg, txt_header, mapsch;
	ProgressBar prog;
	MyVideo svv;
	ImageView shadw;
	private DisplayMetrics dm;
	private StateListDrawable btnbg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		dm=getResources().getDisplayMetrics();
		txt_title=findViewById(R.id.mainanTextView1);
		txt_desc=findViewById(R.id.mainanTextView2);
		btn=findViewById(R.id.mainanButton);
		mapsch=findViewById(R.id.mainanmapscheme);
		shadw=(ImageView) findViewById(R.id.blur_shadow);
		svv=(MyVideo) findViewById(R.id.mainanVideo);
		txt_header=findViewById(R.id.mainanTxt);
		gradbg=findViewById(R.id.mainanGradBg);
		prog=(ProgressBar) findViewById(R.id.mainanProgressBar1);
		btnlabel=findViewById(R.id.button_labelicon);
		btn.setTag(false);
		shadw.setColorFilter(Color.parseColor("#37C500"));
		
		btnbg=(StateListDrawable) btn.getBackground();
		prog.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
		try {
			MediaPlayer mp=svv.mplayr;
			AssetFileDescriptor rfd=getResources().openRawResourceFd(R.raw.mapsanimated);
			mp.setDataSource(rfd.getFileDescriptor(), rfd.getStartOffset(), rfd.getLength());
			mp.prepare();
			
		} catch(Exception e) {}

		new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					startAnimation();
				}
			}, 1000);
		btn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					// buat button/my location tidak bereaksi saat di klik
					if((boolean)btn.getTag()) return;
					// buat button/my location tidak bereaksi saat di klik
					btn.setTag(true);
					// hilangkan button label dan icon serta scale shadow menjadi kecil
					btnlabel.animate().setDuration(500).alpha(0).start();
					shadw.animate().setStartDelay(500).setDuration(1000).alpha(0.6f).scaleX(0.1f).scaleY(0.3f).start();
					// Animator untuk scale ke bentuk bulat
					ValueAnimator btnscale=ValueAnimator.ofFloat(dp2px(350), dp2px(60));
					btnscale.setDuration(1000);
					btnscale.setStartDelay(500);
					btnscale.setInterpolator(new AccelerateDecelerateInterpolator());
					btnscale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
							@Override
							public void onAnimationUpdate(ValueAnimator p1) {
								RelativeLayout.LayoutParams blp=(RelativeLayout.LayoutParams) btn.getLayoutParams();
								blp.width=Math.round(p1.getAnimatedValue());
								btn.setLayoutParams(blp);
							}
						});
					btnscale.start();
					// Animator untuk merubah warna
					final ValueAnimator animcolr = ValueAnimator.ofArgb(Color.parseColor("#37C500"), Color.parseColor("#4285F4"));
					animcolr.setDuration(3000);
					animcolr.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(ValueAnimator valueAnimator) {
								((GradientDrawable)btnbg.getCurrent()).setColor((Integer)valueAnimator.getAnimatedValue());
							}
						});
					// munculin progressBar
					prog.animate().setStartDelay(1000).setDuration(500).alpha(1).start();
					// pindah button serta scale ke ukuran kecil
					btn.animate().setStartDelay(4000).setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(0.3f).scaleY(0.3f).translationY(dp2px(217)).translationX(dp2px(100)).setDuration(2000).setListener(new Animator.AnimatorListener(){
							@Override
							public void onAnimationStart(Animator p1) {
								// ==== saat button mulai bergeser posisi
								// rubah warna button
								animcolr.start();
								// hilangkan shadow
								shadw.animate().setStartDelay(0).setDuration(500).alpha(0).start();
								// putar animasi maps
								svv.mplayr.start();
								// hilangkan text header
								txt_header.animate().setDuration(1500).scaleX(0).scaleY(0).start();
								// hilangan background utama dan garis map sehingga maps bisa terlihat
								gradbg.animate().setDuration(2000).alpha(0).start();
								mapsch.animate().setDuration(1000).alpha(0).start();
								// hilangkan progress
								prog.animate().setStartDelay(0).alpha(0).setDuration(50).start();
							}
							@Override
							public void onAnimationEnd(Animator p1) {
								((GradientDrawable)btnbg.getCurrent()).setStroke(15, Color.WHITE);
							}
							@Override
							public void onAnimationCancel(Animator p1) {
							}
							@Override
							public void onAnimationRepeat(Animator p1) {
							}
					}).start();
				}
			});
	}

	private void startAnimation() {
		// buat title header ke posisi 100px kebawah dari posisi awal
		txt_title.animate().translationY(100).setDuration(0).start();
		// buat title header kembali ke posisi awal juga biar dia terlihat (visible)
		txt_title.animate().setDuration(1000).translationY(0).alpha(1).start();
		// buat desc header ke posisi 100px kebawah dari posisi awal
		txt_desc.animate().translationY(100).translationX(50).scaleX(3f).scaleY(3f).setDuration(0).start();
		// buat desc header kembali ke posisi awal juga buat dia terlihat (visible)
		txt_desc.animate().setDuration(1000).translationY(0).scaleX(1).scaleY(1).translationX(0).alpha(1).start();
		// buat posisi Y button ke bawah layar
		btn.animate().setStartDelay(0).translationY(dm.heightPixels).setInterpolator(new AccelerateInterpolator()).setDuration(0).start();
		// pindahkan posisi Y button ke posisi awal (efek muncul)
		btn.animate().setStartDelay(0).translationY(0).setDuration(1000).alpha(1).setInterpolator(Build.VERSION.SDK_INT>=21?new PathInterpolator(0.293f,0.701f,0.903f,1.347f):new AccelerateDecelerateInterpolator()).start();
		// Animator untuk scale button dari bulet ke ukuran normal button
		ValueAnimator btnscale=ValueAnimator.ofFloat(dp2px(60), dp2px(350));
		btnscale.setDuration(500);
		btnscale.setStartDelay(1000);
		btnscale.setInterpolator(new AccelerateDecelerateInterpolator());
		btnscale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
				@Override
				public void onAnimationUpdate(ValueAnimator p1) {
					RelativeLayout.LayoutParams blp=(RelativeLayout.LayoutParams) btn.getLayoutParams();
					blp.width = Math.round(p1.getAnimatedValue());
					btn.setLayoutParams(blp);
					// jika bulet sudah berubah jadi button, munculin label dan icon serta munculin bayangan
					if((float)p1.getAnimatedValue()>=dp2px(350)) {
						btnlabel.animate().setDuration(500).alpha(1).start();
						shadw.animate().setDuration(2000).alpha(1).setInterpolator(new DecelerateInterpolator()).start();
					}
				}
			});
		btnscale.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// reset button ke warna dan stroke semula
		GradientDrawable gdc=((GradientDrawable)btnbg.getCurrent());
		gdc.setColor(Color.parseColor("#37C500"));
		gdc.setStroke(0, Color.WHITE);
	}
	
	public float dp2px(float dp){
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}
	
	public static class MyVideo extends TextureView implements TextureView.SurfaceTextureListener {
		public static MediaPlayer mplayr;
		public MyVideo(Context context, AttributeSet attrs) {
			super(context, attrs);
			setSurfaceTextureListener(this);
			mplayr=new MediaPlayer();
		}
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture p1, int p2, int p3) {
			mplayr.setSurface(new Surface(p1));
		}
		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture p1, int p2, int p3) {
		}
		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture p1) {
			return false;
		}
		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture p1) {
		}
	}
}
