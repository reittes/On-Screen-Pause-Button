package com.android.reittes.onscreenpausebutton;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

import android.view.MotionEvent;


/**
 * @author Nicolas Gramlich
 * @since 01:30:15 - 02.04.2010
 */

/**
 * On Screen Pause Button
 * @author Reittes
 */

public class OnScreenPauseButtonActivity extends BaseGameActivity implements IOnMenuItemClickListener{

	private Scene scene;
	private Camera camera;

	private float CAMERA_HEIGHT= 480;
	private float CAMERA_WIDTH= 800;
	
	private BitmapTextureAtlas btnTexture;
	private TextureRegion btn_pauseRegion, btn_playRegion;
	private Sprite pauseBtn;

	
	@Override
	public Engine onLoadEngine() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		engineOptions.getRenderOptions().disableExtensionVertexBufferObjects();
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		btnTexture= new BitmapTextureAtlas(64,32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		btn_pauseRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnTexture, this, "gfx/pause.png", 0 ,0);
		btn_playRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnTexture, this, "gfx/play.png", 32 ,0);
		
		getEngine().getTextureManager().loadTexture(btnTexture);
	}

	@Override
	public Scene onLoadScene() {
		getEngine().registerUpdateHandler(new FPSLogger());
		
		scene= new Scene();
				
		final Shape rectSprite= new Rectangle(200, 230, 50, 50);
		rectSprite.setColor(1, 1, 1);
		rectSprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(
				new PathModifier(0.9f, new Path(2).to(200, 230).to(400,230), EaseSineInOut.getInstance()),
				new PathModifier(0.9f, new Path(2).to(400, 230).to(200,230), EaseSineInOut.getInstance())
				)));
		scene.attachChild(rectSprite);
		
		pauseBtn= new Sprite(CAMERA_WIDTH- this.btn_pauseRegion.getWidth()-100, 50, this.btn_pauseRegion){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
	   				 this.setVisible(false);
	   				 scene.setChildScene(pauseScene(), false, true, true);
	   			 }
	   			 return true;
       	}};
       	pauseBtn.setScale(2);
       	scene.attachChild(pauseBtn);
       	scene.registerTouchArea(pauseBtn);
		
		return scene;
	}
	
	// Menu Scene with Play Button 
	
	private MenuScene pauseScene(){
		final MenuScene pauseGame= new MenuScene(camera);
		
		final SpriteMenuItem btnPlay = new SpriteMenuItem(1, this.btn_playRegion);
		btnPlay.setPosition(CAMERA_WIDTH- this.btn_pauseRegion.getWidth()-100, 50);
		btnPlay.setScale(2);
		pauseGame.addMenuItem(btnPlay);
		
		pauseGame.setBackgroundEnabled(false);
		pauseGame.setOnMenuItemClickListener(this);
		return pauseGame;
	}
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch(arg1.getID()){
		case 1:
			if(scene.hasChildScene()){
				scene.clearChildScene();
				pauseBtn.setVisible(true);
			}
			return true;
		default:
			return false;
		}
	}

	
}