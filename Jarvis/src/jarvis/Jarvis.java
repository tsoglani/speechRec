/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import jaco.mp3.player.MP3Player;
import java.io.File;
//import com.sun.speech.freetts.Voice;
//import com.sun.speech.freetts.VoiceManager;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import weather.Weather;

/**
 *
 * @author tsoglani
 */
public class Jarvis {

    /**
     * @param args the command line arguments
     */
    private Microphone mic;
    private GSpeechDuplex duplex;
    public static final String name = "";
    private static Weather weather;
    private String output;
    private String bigOutput = "";
    public static final String GeoCiyyLocation = "/home/tsoglani/NetBeansProjects/Jarvis/resources/location/GeoLiteCity.dat";

    public static void main(String[] args) {
        new Jarvis();

    }
GSpeechResponseListener gSpeechListener;
    public Jarvis() {
        dospeak("hello sir");
//        playAudio();
        weather = new Weather();
        
      gSpeechListener=  new GSpeechResponseListener() {
//            String old_text = "";

            public void onResponse(GoogleResponse gr) {

                System.out.println(gr.getResponse());
                if (gr.getResponse() == null || gr.getResponse().replaceAll(" ", "").equalsIgnoreCase("")) {
                    processRespond(bigOutput);
                    System.out.println("output=" + output + "  ::prevOut=" + bigOutput);

                    bigOutput = "";
                    output = "";
                    counter = 0;

                } else {
                    output = gr.getResponse();
                }
                if (bigOutput == null || bigOutput.length() <= output.length()) {
                    bigOutput = output;
//                    System.out.println(output);
                } 
                    if (thread == null) {
                        thread = new Thread() {
                            @Override
                            public void run() {
                                counter = totalWaitTime;

                                try {
                                    while (counter > 0) {
                                        sleep(sleepTime);
                                        counter -= sleepTime;
                                    }
                                    if (output != null || !output.replaceAll(" ", "").equals("")) {
                                        output = "";
                                        if (bigOutput != null && !bigOutput.replaceAll(" ", "").equals("")) {
                                            if (!lastProcessRespond.equals(bigOutput)) {
                                                processRespond(bigOutput);

                                            }
                                            lastProcessRespond="";
                                            bigOutput = "";
                                            deActivate();

                                            activate();
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                thread = null;
                            }
                        };
                        thread.start();
                    } else {
                        counter = totalWaitTime;

                    }
                

//                if (gr != null && gr.getResponse() != null && !output.replaceAll(" ", "").equals("")) {
//                    if (thread == null) {
//                        thread = new Thread() {
//                            @Override
//                            public void run() {
//                                try {
//                                    sleep(1000);
//                                    processRespond(output);
//
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                                thread = null;
//                            }
//                        };
//                        thread.start();
//                    }
////                    processRespond(output);
//                }
            }
        };
      
              activate();


    }
    private Thread thread;
    private int counter;
    private int sleepTime = 10;
    private final int totalWaitTime = 2000;

    public void activate() {
        mic = new Microphone(FLACFileWriter.FLAC);

        duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

        duplex.setLanguage("en");

        try {
            mic.open();
            duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        duplex.addResponseListener(gSpeechListener);
    }

    String respondText = "";
    private String lastProcessRespond = "";

    private void processRespond(String respond) {
        lastProcessRespond = respond;
        System.out.println("respond::" + respond);
        if (respondText.replaceAll(" ", "").contains(name + "hi") || respondText.replaceAll(" ", "").contains(name + "hello")
                || respondText.replaceAll(" ", "").contains("hi" + name) || respondText.replaceAll(" ", "").contains("hello" + name)) {
            dospeak("wellcome sir");
            respondText = "";
        } else if (respond.replaceAll(" ", "").contains(name + "aresokind") || respond.replaceAll(" ", "").contains("aresokind" + name)) {
            dospeak("Thank you sir.");
            respondText = "";
        } else if (respond.replaceAll(" ", "").contains(name + "hi") || respond.replaceAll(" ", "").contains(name + "hello")
                || respond.replaceAll(" ", "").contains("hi" + name) || respond.replaceAll(" ", "").contains("hello" + name)) {
            dospeak("wellcome sir");
            respondText = "";
        } else if (respond.replaceAll(" ", "").contains(name + "stop") || respond.replaceAll(" ", "").contains(name + "cancel")) {
            stopSpeak();
            respondText = "";
        } else if (respond.replaceAll(" ", "").contains("weatherin" + name) || respond.replaceAll(" ", "").contains(name + "weatherin")) {
            String[] list = respond.split("weather in");
            try {
                if (list.length > 1) {
                    String curentCity = list[1];
                    if (curentCity.length() > 1) {
                        if (curentCity.contains(" of ")) {
                            String[] surentList = curentCity.split(" of ");
                            curentCity = surentList[0];
                            String countryCode = surentList[1];
                            String weatherInfo = weather.getWeather(curentCity.substring(1, curentCity.length()), countryCode);
                            dospeak(weatherInfo);
                        } else if (curentCity != null && !curentCity.equals("")) {
                            String weatherInfo = weather.getWeather(curentCity.substring(1, curentCity.length()));
                            dospeak(weatherInfo);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            dospeak("wellcome sir");

//            String weatherInfo = weather.getWeather(curentCity);
//            dospeak(weatherInfo);
            respondText = "";

        } else if (respond.replaceAll(" ", "").contains("weather" + name) || respond.replaceAll(" ", "").contains(name + "weather")) {

            String weatherInfo;
            if (respond.replaceAll(" ", "").contains("forecast")) {
                weatherInfo = weather.getForecastWeatherAtCity("Ierapetra");

            } else {
                weatherInfo = weather.getWeather("Ierapetra");
            }
            dospeak(weatherInfo);
            respondText = "";

        } else if (respond.replaceAll(" ", "").contains("whatis") && respond.replaceAll(" ", "").contains(name)
                || respond.replaceAll(" ", "").contains(name + "whatis")
                || respond.replaceAll(" ", "").contains("what's") && respond.replaceAll(" ", "").contains(name)
                || respond.replaceAll(" ", "").contains(name + "what's")
                || respond.replaceAll(" ", "").contains("search") && respond.replaceAll(" ", "").contains(name)) {

            String spStr = null;
            try {

                if (respond.replaceAll(" ", "").contains("whatis")) {
                    spStr = getFromWiki(respond.split("what is")[1]);
                } else if (respond.replaceAll(" ", "").contains("what's")) {
                    spStr = getFromWiki(respond.split("what's")[1]);
                } else if (respond.replaceAll(" ", "").contains("search")) {
                    spStr = getFromWiki(respond.split("search")[1]);
                }
                if (spStr != null) {
//                    System.out.println("getFromWiki:" + spStr);
                    dospeak(spStr);

                }
            } catch (Exception ex) {
                Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
            }
            respondText = "";
        } else if (respond.replaceAll(" ", "").contains("playaudio" + name)
                || respond.replaceAll(" ", "").contains(name + "playaudio")
                || respond.replaceAll(" ", "").contains("playsound" + name)
                || respond.replaceAll(" ", "").contains(name + "playsound")
                || respond.replaceAll(" ", "").contains("playmusic" + name)
                || respond.replaceAll(" ", "").contains(name + "playmusic")
                || respond.replaceAll(" ", "").contains("playsong" + name)
                || respond.replaceAll(" ", "").contains(name + "playsong")) {

            dospeak("right away sir.");
            playAudio();
            respondText = "";

        } else if (respond.replaceAll(" ", "").contains("nextsong")
                || respond.replaceAll(" ", "").contains("next")) {
            nextSong();

        } else if (respond.replaceAll(" ", "").contains("previous")
                || respond.replaceAll(" ", "").contains("previoussong")) {
            previousSong();
            respondText = "";

        } else if (respond.replaceAll(" ", "").contains("howareyou" + name) || respond.replaceAll(" ", "").contains(name + "howareyou")) {
            dospeak("I am fine sir, thanks for asking. Would you like something else.");
        } else if (!respondText.contains(respond)) {
            respondText += " " + respond;
        }
    }

    private String getFromWiki(String searchingText) throws MalformedURLException, IOException {
//https://en.wikipedia.org/w/api.php?action=opensearch&search=java-programming&limit=1&format=json
        String[] list = searchingText.split(" ");
        String searchingAddingString = "";
        for (int i = 0; i < list.length; i++) {
            searchingAddingString += list[i];
            if (i < list.length - 1) {
                searchingAddingString += "-";
            }
        }
        if (searchingAddingString.equals("")) {
            return null;
        }

        String urlString = "https://en.wikipedia.org/w/api.php?action=opensearch&search=" + searchingAddingString + "&limit=1&format=json";
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = org.apache.commons.io.IOUtils.toString(in, encoding);
        System.out.println(body);
//        body.replaceAll("\\[", "@@@");
//        body.replaceAll("\\]", "@@@");

        String[] listBody = body.split("\\[\"");
        try {
            String output = listBody[3].substring(0, listBody[3].length() - 3);
            System.out.println(output);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

//    private static final String VOICENAME_kevin = "kevin";
//    Voice voice;
//    VoiceManager voiceManager;
    private void stopSpeak() {
        if (child != null) {
            child.destroyForcibly();
        }

        if (mp3Player != null) {
            mp3Player.stop();
        }

    }
    Process child;
////sudo apt-get install espeak

    public void dospeak(String speaktext) {
        try {
            //        if (speaktext == null || speaktext.replaceAll(" ", "").equals("")) {
//            return;
//        }
//
//        if (voice != null) {
//            voice.deallocate();
//        }
//        if (voiceManager == null) {
//            voiceManager = VoiceManager.getInstance();
//        }
//        voice = voiceManager.getVoice(VOICENAME_kevin);
//        voice.allocate();
//
//        voice.speak(speaktext);

            if (child != null) {
                child.destroyForcibly();
            }

////sudo apt-get install espeak
            Runtime.getRuntime().exec(new String[]{"espeak", '\"' + speaktext + '\"'});

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deActivate() {
        if (mic != null && mic.getTargetDataLine().isOpen()) {

            mic.close();
        }
        if (duplex != null) {
            try {
                if(gSpeechListener!=null)
                duplex.removeResponseListener(gSpeechListener);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            duplex = null;
        }
        System.gc();
//        if (voice != null) {
//            voice.deallocate();
//        }
//        voice = null;

    }
    private MP3Player mp3Player;
    private String musicFilesLocation = "/media/tsoglani/tsoglani1/Music";
    File musicFile = new File(musicFilesLocation);
    int musicCounterID = ((int) (Math.random() * (musicFile.list().length - 1)));
    private boolean isGoingToPlayNext = false;
    Thread playerThread;

    private void playAudio() {

        try {
            if (musicCounterID >= musicFile.list().length) {
                musicCounterID = 0;
            }
//            File cf = new File(musicFilesLocation + "/" + musicFile.list()[musicCounterID++]);
//            if (cf.isDirectory() || !cf.exists()) {
//                playAudio();
//            }
//            String bip = cf.getAbsolutePath();
//            System.out.println(bip);
////            AudioInputStream audioIn = AudioSystem.getAudioInputStream(cf);
////            Clip clip = AudioSystem.getClip();
////            clip.open(audioIn);
////            clip.start();
            if (mp3Player != null && !mp3Player.isStopped()) {
                mp3Player.stop();
            }

            mp3Player = new MP3Player();

            String[] files = musicFile.list();
            for (int i = 0; i < files.length; i++) {
                String title = files[i];
                File cf = new File(musicFilesLocation + "/" + title);
                if (cf.isFile()) {
                    mp3Player.addToPlayList(cf);
                }

            }
            mp3Player.play();

        } catch (Exception ex) {
            Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void previousSong() {
        if (mp3Player != null) {
            mp3Player.skipBackward();
        }

    }

    private void nextSong() {
        if (mp3Player != null) {
            mp3Player.skipForward();
        }

    }

}
