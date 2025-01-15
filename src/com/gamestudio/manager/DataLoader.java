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
    private Hashtable<String, Clip> musics; 
    
    /*private Hashtable<String, FrameImage> megaManframes;
    private Hashtable<String, FrameImage> woodManframes;
    private Hashtable<String, FrameImage> menuFrames;
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
    
    public void LoadData()throws IOException {
        LoadPhysMap();
        LoadMusics();
        /* 
        LoadWoodManFrames();
        LoadMegaManFrames();
        LoadMenuFrames();
        LoadAnimation();
        */
        
    }
    
    public void LoadMusics() throws IOException {
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
    
    public void LoadPhysMap() throws IOException {
        
        FileReader fr = new FileReader("");
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
    /*
    public void LoadWoodManFrames() throws IOException {
        
        FileReader fr = new FileReader("Assets/woodmanframes.txt");
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        String imageName = null;
        BufferedImage image = null;
        FrameImage frame = new FrameImage();

        line = br.readLine();
        while ((imageName = br.readLine()) != null) {
            image = ImageIO.read(new File(line + imageName + ".png"));
            
            frame.setName(imageName);
            frame.setImage(image);
            instance.woodManframes.put(frame.getName(), frame);
        }
        br.close();
    }

    public void LoadMegaManFrames() throws IOException {
        
        FileReader fr = new FileReader("Assets/megamanframes.txt");
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        String imageName = null;
        BufferedImage image = null;
        FrameImage frame = new FrameImage();
        line = br.readLine();
        while ((imageName = br.readLine()) != null) {
            image = ImageIO.read(new File(line + imageName + ".png"));
            
            frame.setName(imageName);
            frame.setImage(image);
            instance.megaManframes.put(frame.getName(), frame);
        }
        br.close();
    }

    public void LoadMenuFrames() throws IOException {
        
        FileReader fr = new FileReader("Assets/menuframes.txt");
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        String imageName = null;
        BufferedImage image = null;
        FrameImage frame = new FrameImage();
        line = br.readLine();
        while ((imageName = br.readLine()) != null) {
            image = ImageIO.read(new File(line + imageName + ".png"));
            
            frame.setName(imageName);
            frame.setImage(image);
            instance.menuframes.put(frame.getName(), frame);
        }
        br.close();
    }*/
}
