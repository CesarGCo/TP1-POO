package com.gamestudio.effect;


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
    private String name;
    private int currentFrame;
    private long beginTime;
    private boolean isRepeated;
    private boolean drawRectFrame;
    private final ArrayList<FrameImage> frameImages;
    private final ArrayList<Boolean> ignoreFrames;
    private final ArrayList<Double> delayFrames;

    public Animation(){
        delayFrames = new ArrayList<>();
        beginTime = 0;
        currentFrame = 0;

        ignoreFrames = new ArrayList<>();

        frameImages = new ArrayList<>();

        drawRectFrame = false;

        isRepeated = true;
    }

    public Animation(Animation animation){
        name = animation.name;
        beginTime = animation.beginTime;
        currentFrame = animation.currentFrame;
        drawRectFrame = animation.drawRectFrame;
        isRepeated = animation.isRepeated;

        delayFrames = new ArrayList<>();
        delayFrames.addAll(animation.delayFrames);

        ignoreFrames = new ArrayList<>();
        ignoreFrames.addAll(animation.ignoreFrames);

        frameImages = new ArrayList<>();
        for(FrameImage f : animation.frameImages){
            frameImages.add(new FrameImage(f));
        }
    }

    public void setIsRepeated(boolean isRepeated){
        this.isRepeated = isRepeated;
    }

    public boolean getIsRepeated(){
        return isRepeated;
    }

    public boolean isIgnoreFrame(int id){
        return ignoreFrames.get(id);
    }

    public void setIgnoreFrame(int id){
        if(id >= 0 && id < ignoreFrames.size())
            ignoreFrames.set(id, true);
    }

    public void unIgnoreFrame(int id){
        if(id >= 0 && id < ignoreFrames.size())
            ignoreFrames.set(id, false);
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setCurrentFrame(int currentFrame){
        if(currentFrame >= 0 && currentFrame < frameImages.size())
            this.currentFrame = currentFrame;
        else this.currentFrame = 0;
    }
    public int getCurrentFrame(){
        return this.currentFrame;
    }

    public void reset(){
        currentFrame = 0;
        beginTime = 0;
    }

    public void add(FrameImage frameImage, double timeToNextFrame){
        ignoreFrames.add(false);
        frameImages.add(frameImage);
        delayFrames.add(timeToNextFrame);

    }

    public void setDrawRectFrame(boolean b){
        drawRectFrame = b;
    }


    public BufferedImage getCurrentImage(){
        return frameImages.get(currentFrame).getImage();
    }

    public void Update(long deltaTime){
        if(beginTime == 0) beginTime = deltaTime;
        else{
            if(deltaTime - beginTime > delayFrames.get(currentFrame)){
                nextFrame();
                beginTime = deltaTime;
            }
        }

    }

    public boolean isLastFrame(){
        return currentFrame == frameImages.size() - 1;
    }

    private void nextFrame(){
        if(currentFrame >= frameImages.size() - 1){

            if(isRepeated) currentFrame = 0;
        }
        else currentFrame++;

        if(ignoreFrames.get(currentFrame)) nextFrame();

    }

    public void flipAllImage(){
        for (FrameImage frameImage : frameImages) {

            BufferedImage image = frameImage.getImage();

            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(), 0);

            AffineTransformOp op = new AffineTransformOp(tx,
                    AffineTransformOp.TYPE_BILINEAR);
            image = op.filter(image, null);

            frameImage.setImage(image);
        }

    }

    public void draw(int x, int y, Graphics2D g2){
        FrameImage currentFrameImage = frameImages.get(currentFrame);
        currentFrameImage.draw(x, y, g2);
    }
}