package com.gamestudio.manager;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import com.gamestudio.effect.Animation;
import com.gamestudio.effect.FrameImage;;

// A classe DataLoader tem a função de carregar todos os assets do projeto
// armazenando todos eles em tabelas Hash para facilitar o acesso
public class DataLoader {
    private static DataLoader instance = null;

    private Hashtable<String, Clip> sounds; 
    private Hashtable<String, FrameImage> frames;
    private Hashtable<String, Animation> animations;
    
    private int[][] phys_map;
    
    
    private DataLoader() {}

    public static DataLoader getInstance(){
        if(instance == null)
            instance  = new DataLoader();
        return instance;
    }
    
    public Clip getSound(String name){
        return instance.sounds.get(name);
    }
    
    public Animation getAnimation(String name){
        
        Animation animation = new Animation(instance.animations.get(name));
        return animation;
        
    }
    
    public FrameImage getFrameImage(String name){

        FrameImage frameImage = new FrameImage(instance.frames.get(name));
        return frameImage;

    }
    
    public int[][] getPhysicalMap(){
        return instance.phys_map;
    }
    
    // Carrega todos os Assets do jogo
    public void LoadData()throws IOException {
        LoadPhysMap();
        LoadSounds();
        LoadFrame();
        LoadAnimation();
    }

    // Realiza o carregamento dos sons com auxílio de um arquivo de entrada    
    public void LoadSounds() throws IOException {
        this.sounds = new Hashtable<String, Clip>();
        
        FileReader fr = new FileReader("Assets/sounds.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        String soundName = null;
        Clip clip = null;
        File audioFile = null;
        while((soundName = br.readLine()) != null) {
            try {
                audioFile = new File(line + soundName + ".wav");
                AudioInputStream audioStream =AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance.sounds.put(soundName, clip);
        }
        br.close();
    }
    

    // Realiza o carregamento da matriz de colisão presente no arquivo phys_map.txt
    public void LoadPhysMap() throws IOException {
        
        FileReader fr = new FileReader("Assets/phys_map.txt");
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
        
        br.close();
        
    }
    
    // Realiza o carregamento das animações com auxílio de um arquivo de entrada    
    public void LoadAnimation() throws IOException {
        
        animations = new Hashtable<String, Animation>();
        
        FileReader fr = new FileReader("Assets/animations.txt");
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        if(br.readLine()==null) {
            System.out.println("No data");
            br.close();
            throw new IOException();
        }
        else {
            
            fr = new FileReader("Assets/animations.txt");
            br = new BufferedReader(fr);
            
            while((line = br.readLine()).equals(""));
            int n = Integer.parseInt(line);
            
            for(int i = 0;i < n; i ++){
                try {
                    Animation animation = new Animation();
                    
                    while((line = br.readLine()).equals(""));
                    animation.setName(line);
                    while((line = br.readLine()).equals(""));
                    String[] str = line.split(" ");
                    
                    for(int j = 0;j<str.length;j+=2)
                        animation.add(getFrameImage(str[j]), Double.parseDouble(str[j+1]));
                    
                    instance.animations.put(animation.getName(), animation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        }
        
        br.close();
    }

    // Realiza o carregamento das imagens com auxílio de um arquivo de entrada   
    public void LoadFrame() throws IOException{
        
        this.frames = new Hashtable<String, FrameImage>();
        
        FileReader fr = new FileReader("Assets/frames.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        String imageName = null;
        BufferedImage image = null;
        while ((line = br.readLine()) != null) {
            imageName = br.readLine();
            while(!imageName.equals("0")) {
                try {
                    image = ImageIO.read(new File(line + imageName + ".png"));
                    FrameImage frame = new FrameImage(imageName, image);
                    instance.frames.put(frame.getName(), frame);
                    imageName = br.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        br.close();
    }
}
