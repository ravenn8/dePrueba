package vista;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.Timer;
import com.jme3.ui.Picture;

public class Display {
    
    private Node displayNode;    
    private BitmapText pos;
    private BitmapText chronograph;
    private BitmapText posText;
    private BitmapFont posFont;
    private BitmapFont textFont;
    private BitmapFont chronographFont;
    private AssetManager assetManager;
    private AppSettings settings;
    private Node guiNode;
    private Timer timer;
    
    public Display(AssetManager assetManager, AppSettings settings,Node guiNode,Timer timer){
        
        this.assetManager = assetManager;
        this.settings = settings;
        this.guiNode = guiNode;        
        this.displayNode = new Node("Display");
        this.timer = timer;
    }
    
    public void addDisplay(int xDisplay,int yDisplay,float scaleValueDisplay,int xPosText, int yPosText, float scaleValuePosText,int xPos, int yPos,float scaleValuePos, int xChronograph, int yChronograph, float scaleValueChronograph){
        
        float minDimension = Math.min(settings.getWidth(),settings.getHeight());      
        
        //añadimos el cronoemtro
        chronographFont = assetManager.loadFont("Interface/Fonts/DS-Digital.fnt");
        chronograph = new BitmapText(chronographFont, false);        
        chronograph.setSize(minDimension/scaleValueChronograph);      // font size
        chronograph.setColor(ColorRGBA.White);                            // font color        
        chronograph.setText("00:00");                                    // the text
        chronograph.setLocalTranslation(xChronograph-(chronograph.getLineWidth()/2),yChronograph,0);     // position
        guiNode.attachChild(chronograph);
        
        //añadimos el la posicion
        posFont = assetManager.loadFont("Interface/Fonts/MotorOil1937M54.fnt");
        pos = new BitmapText(posFont, false);        
        pos.setSize(minDimension/scaleValuePos);      // font size
        pos.setColor(ColorRGBA.Yellow);                            // font color
        pos.setText("1");                                    // the text
        pos.setLocalTranslation(xPos,yPos,0);     // position
        guiNode.attachChild(pos);
        
        //añadimos el texto POS
        textFont = assetManager.loadFont("Interface/Fonts/DejaVuSansCondensed.fnt");
        posText = new BitmapText(textFont, false);       
        posText.setSize(minDimension/scaleValuePosText);      // font size
        posText.setColor(ColorRGBA.White);                            // font color
        posText.setText("POS");                                    // the text
        posText.setLocalTranslation(xPosText,yPosText,0);     // position
        guiNode.attachChild(posText);
        
        //Agregar fondo marcador        
        Picture display = new Picture("display");
        display.setImage(assetManager, "Textures/Display/gauge.png", true);        
          
        display.setWidth(minDimension/scaleValueDisplay);
        display.setHeight(minDimension/scaleValueDisplay);        
        display.setPosition(0,0);
        display.center();
        display.move(xDisplay,yDisplay, -1); //-1 para estar debajo de la aguja        
        guiNode.attachChild(display);
        
        //Agregar aguja
        Picture arrow = new Picture("arrow");
        arrow.setImage(assetManager, "Textures/Display/arrow.png", true);        
        arrow.setWidth(minDimension/scaleValueDisplay);
        arrow.setHeight(minDimension/scaleValueDisplay);
        arrow.setPosition(0,0);
        arrow.center();
        arrow.move(0, 0, 1); //1 para poner por encima del marcador                
        
        displayNode.attachChild(arrow);        
              
        guiNode.attachChild(displayNode);
        this.displayNode.move(xDisplay,yDisplay,0);       
    }
    
    public void startChronograph(){
        this.chronograph.setText("00:00");
        this.timer.reset();        
    }
    
    public void updateChronograph(){
        float totalSeconds = this.timer.getTimeInSeconds();
        int seconds = (int)totalSeconds%60;
        int minutes = (int)totalSeconds/60;
        if (seconds < 10 && minutes < 10){
            this.chronograph.setText("0"+minutes+":0"+seconds);            
        }
        else if(seconds < 10){
            this.chronograph.setText(minutes+":0"+seconds);     
        }
        else if (minutes < 10){
            this.chronograph.setText("0"+minutes+":"+seconds);     
        }
        else{
            this.chronograph.setText(minutes+":"+seconds);     
        }
    }
    
    public void updatePosition(int pos){
        if (pos > 0){
            this.pos.setText(""+pos);
        }
    }
    
    public void updateDisplay(float speed){
        
        if(isDisplayAdded()){ //comprobamos si el display se ha creado, en caso contratio no hacemos nada            
                     
            if (speed > 200){
                speed=200;
            }
            else if (speed < 0){
                speed=0;
            }            
            float actual_gauge_speed = displayNode.getWorldRotation().getZ();          
            actual_gauge_speed = (float)(46.2606 + (-154.202f *actual_gauge_speed));
            float offset = actual_gauge_speed - speed;                           
            displayNode.rotate(0, 0,offset*0.022185f);  
        }
    }
    
    public boolean isDisplayAdded(){
        if(displayNode.getQuantity() >= 1){
            return true;
        }
        else{
            return false;
        }
    }
}
