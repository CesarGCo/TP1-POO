package com.gamestudio.manager;


import java.applet.Applet;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;;

public class DataLoader {
    private static int NUM_SOUNDS = 1;

    private static DataLoader instance = null;
    private String frameFile = "Assets/frames.txt";
    private String physmapfile = "Assets/phys_map.txt";
    private Hashtable<String, Clip> musics; 
    
    /*private Hashtable<String, FrameImage> frames;
    private Hashtable<String, Animation> animations;*/
    
    private int[][] phys_map;
    
    
    private DataLoader() {}

    public static DataLoader getInstance(){
        if(instance == null)
            instance  = new DataLoader();
        return instance;
    }
    
    public Clip getMusic(String name){
        return instance.musics.get(name);
    }
    
    /*public Animation getAnimation(String name){
        
        Animation animation = new Animation(instance.animations.get(name));
        return animation;
        
    }*/
    
    /*public FrameImage getFrameImage(String name){

        FrameImage frameImage = new FrameImage(instance.frameImages.get(name));
        return frameImage;

    }*/
    
    public int[][] getPhysicalMap(){
        return instance.phys_map;
    }
    
    public void LoadData()throws IOException{
        LoadPhysMap();
        LoadMusics();
        /* 
        LoadFrame();
        LoadAnimation();
        LoadBackgroundMap();
        LoadSounds(); */
        
    }
    
    public void LoadMusics() throws IOException{
        instance.musics = new Hashtable<>();
        for(int i = 0; i < NUM_SOUNDS; i ++){
            Clip clip = null;
            try {
                File audioFile = new File("Assets/Musics/Music-" + (i + 1) + ".wav");

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                clip = AudioSystem.getClip();
            } catch (Exception e) {
                e.printStackTrace();
            }
        
            instance.musics.put("Music-" + (i + 1) + ".wav", clip);
        }
    }
    
    public void LoadPhysMap() throws IOException{
        
        FileReader fr = new FileReader(physmapfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        line = br.readLine();
        int numberOfRows = Integer.parseInt(line);
        line = br.readLine();
        int numberOfColumns = Integer.parseInt(line);
            
        
        instance.phys_map = new int[numberOfRows][numberOfColumns];
        
        for(int i = 0;i < numberOfRows;i++){
            line = br.readLine();
            String [] str = line.split(" ");
            for(int j = 0;j<numberOfColumns;j++)
                instance.phys_map[i][j] = Integer.parseInt(str[j]);
        }
        
        /*
        for(int i = 0;i < numberOfRows;i++){
            
            for(int j = 0;j<numberOfColumns;j++)
                System.out.print(" "+instance.phys_map[i][j]);
            
            System.out.println();
        }*/
        
        br.close();
        
    }
    
    /*public void LoadAnimation() throws IOException {
        
        animations = new Hashtable<String, Animation>();
        
        FileReader fr = new FileReader(animationfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        if(br.readLine()==null) {
            System.out.println("No data");
            throw new IOException();
        }
        else {
            
            fr = new FileReader(animationfile);
            br = new BufferedReader(fr);
            
            while((line = br.readLine()).equals(""));
            int n = Integer.parseInt(line);
            
            for(int i = 0;i < n; i ++){
                
                Animation animation = new Animation();
                
                while((line = br.readLine()).equals(""));
                animation.setName(line);
                
                while((line = br.readLine()).equals(""));
                String[] str = line.split(" ");
                
                for(int j = 0;j<str.length;j+=2)
                    animation.add(getFrameImage(str[j]), Double.parseDouble(str[j+1]));
                
                instance.animations.put(animation.getName(), animation);
                
            }
            
        }
        
        br.close();
    }*/
    
    /*public void LoadFrame() throws IOException{
        
        this.frames = new Hashtable<String, FrameImage>();
        
        FileReader fr = new FileReader(frameFile);
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        String imageName = null;
        String name = null;
        BufferedImage image = null;

        while ((line = br.readLine()) != null) {
            for(int i = 0; imageName != "0"; i++) {
                imageName = br.readLine();
                image = ImageIO.read(new File(line + imageName + ".png"));
                name = imageName;
            }
            FrameImage frame = new FrameImage();
            frame.setName(name);
            frame.setImage(image);
            instance.frames.put(frame.getName(), frame);
        }
        br.close();
    }*/
}
