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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import weather.Weather;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.StringTokenizer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;

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
    private Process findMobileProcess;
    private Process speechProcess;
    private String musicFilesLocation = "/media/tsoglani/tsoglani1/Music";
    public static final String GeoCiyyLocation = "/home/tsoglani/NetBeansProjects/Jarvis/resources/location/GeoLiteCity.dat";
    private final String findMobileScr = "/home/tsoglani/NetBeansProjects/Jarvis/resources/FindMobile.jar";
    private GSpeechResponseListener gSpeechListener;
    private Thread googleResponseThread;
    private int counter;
    private int sleepTime = 10;
    private final int totalWaitTime = 2000;
//    private String respondText = "";
    private String lastProcessRespond = "";
    private MP3Player mp3Player;
    File musicFile = new File(musicFilesLocation);
    int musicCounterID = ((int) (Math.random() * (musicFile.list().length - 1)));
    private boolean isGoingToPlayNext = false;
    Thread playerThread;

    // samples ::
    //in ten minutes turn kitchen lights off etc.
    //turn kitchen lights off 
    // how are you
    //hello-hi
    // search - what is ... // wiki search
    // play music-play sound ... next song .. previous song .. cancel
    // find mobile-find phone
    // how old are you
    // bye-goodbye-goodnight
    // about
    // info - informations
    // weather
    // weather in ... (Athens)
    // weather in ... (London at UK)
    // what's your name
    // what's your mission-purpose
    // are you clever
    // is your boss clever
    // turn on-off all except ...
    // turn on-off all 
    // turn on-off all in n time except ...
    // turn on-off all in n time  
// what day is today 
    //time
    //date
    //alarm in N time --- example alarm in 30 seconds
    //alarm at specific time --- example alarm at eight and ten //  alarm at half past ten //alarm at ten to ten
    // time in (City) px London/Athens
//want to do 
    //implement raspberry and create timer methods
    //better speech voice
    //speech mobile commands send to processRespond function (mobile speech commands starts with "speech@@@" must implement it on raspberry side)
    // music festival searching
    public static void main(String[] args) {
        new SH();
        new Jarvis();
//        org.joda.time.DateTimeZone timeZone = org.joda.time.DateTimeZone.forID("Europe/Amsterdam");
//        DateTime now = new DateTime(timeZone);
//        System.out.println(now.toDate());

    }

    public Jarvis() {
//        System.out.println("getTimeIn:" + getTimeIn("Greece"));

        dospeak("hello sir");
        processRespond("date and time in Athens");
//        playAudio();
//        findMobile();
        weather = new Weather();

        gSpeechListener = new GSpeechResponseListener() {
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
                if (googleResponseThread == null) {
                    googleResponseThread = new Thread() {
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
                                        lastProcessRespond = "";
                                        bigOutput = "";
                                        deActivate();

                                        activate();
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            googleResponseThread = null;
                        }
                    };
                    googleResponseThread.start();
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

    private void processRespond(String respond) {
        lastProcessRespond = respond;
        System.out.println("respond::" + respond);
        if (respond.replaceAll(" ", "").contains("time") && respond.replaceAll(" ", "").contains("date") && respond.replaceAll(" ", "").contains("in")) {
            String[] words = respond.split(" ");
            for (String s : words) {
                if (!s.equals("date") || !s.equals("in") || !s.equals("current") || !s.equals("now") || !s.equals("time")) {
                    String info = getDateAndTimeIn(s);
                    if (info != null) {
                        dospeak(info);
                    }
                }
            }
        }else if ((respond.replaceAll(
                " ", "").contains("date" + name) || respond.replaceAll(
                " ", "").contains(name + "date") || respond.replaceAll(
                " ", "").contains(name + "calendar") || respond.replaceAll(
                " ", "").contains("calendar" + name) || respond.replaceAll(
                " ", "").contains(name + "diary") || respond.replaceAll(
                " ", "").contains("diary" + name) || respond.replaceAll(
                " ", "").contains("year" + name) || respond.replaceAll(
                " ", "").contains("month" + name)) && (respond.replaceAll(
                " ", "").contains("curret") || respond.replaceAll(
                " ", "").contains("get") || respond.replaceAll(
                " ", "").contains("local") || respond.replaceAll(
                " ", "").contains("now"))) {//diary	

            dospeak(getDate());
        } else if (respond.replaceAll(
                " ", "").contains("day" + name) && respond.replaceAll(
                " ", "").contains(name + "today")) {

            dospeak(getStringDate() + " sir.");
        } else if (respond.replaceAll(
                " ", "").contains("time") && (respond.replaceAll(
                " ", "").contains("curret") || respond.replaceAll(
                " ", "").contains("get") || respond.replaceAll(
                " ", "").contains("local") || respond.replaceAll(
                " ", "").contains("now"))) {
            dospeak(getCurentTime());
        } else if (respond.replaceAll(" ", "").contains(name + "timein") || respond.replaceAll(" ", "").contains("currenttime")
                || (respond.replaceAll(" ", "").contains("now") && respond.replaceAll(" ", "").contains("time"))) {
            String[] words = respond.split(" ");
            System.out.println("current time");
            for (String s : words) {
                if (!s.equals("time") || !s.equals("in") || !s.equals("current") || !s.equals("now")) {
                    String info = getTimeIn(s, true);
                    if (info != null) {
                        dospeak(info);
                    }
                }
            }
        } else if (respond.replaceAll(" ", "").contains(name + "datein") || respond.replaceAll(" ", "").contains("currentdate")
                || (respond.replaceAll(" ", "").contains("now") && respond.replaceAll(" ", "").contains("date"))) {
            String[] words = respond.split(" ");
            for (String s : words) {
                if (!s.equals("date") || !s.equals("in") || !s.equals("current") || !s.equals("now")) {
                    String info = getDateIN(s, true);
                    if (info != null) {
                        dospeak(info);
                    }
                }
            }
        }  else if (respond.replaceAll(
                " ", "").contains(name + "alarm") || respond.replaceAll(" ", "").contains("alarm" + name)) {
            if (respond.contains("in ") || respond.contains("time")) {
                try {
                    String timeContainsText = null;
                    if (respond.contains("in ")) {
                        timeContainsText = respond.split("in ")[1];
                    } else if (respond.contains("time")) {
                        timeContainsText = respond.split("time")[1];

                    }
                    if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                        dospeak("can you repeat the command sir?");
                        System.out.println("repeat command");
                        return;
                    }

                    boolean found = false;
                    String[] wordsList = timeContainsText.split(" ");
                    for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                        String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);

                        }
                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        }
                        found = true;
                        System.out.println(timeUnit);
                        if (timeUnit.contains("minute")) {
                            if (number > 1) {
                                timeUnit = "minutes";
                            } else {
                                timeUnit = "minute";
                            }
                        } else if (timeUnit.contains("sec") || timeUnit.contains("seconds")) {
                            if (number > 1) {
                                timeUnit = "seconds";
                            } else {
                                timeUnit = "second";
                            }
                        } else if (timeUnit.contains("hour")) {
                            if (number > 1) {
                                timeUnit = "hours";
                            } else {
                                timeUnit = "hour";
                            }
                        } else if (number > 1) {
                            timeUnit = "minutes";
                        } else {
                            timeUnit = "minute";
                        }

                        alarmInNTime(number, timeUnit, true);
                    }
                    if (!found) {
                        System.out.println("Can you repeat the command sir ?");

                        dospeak("Can you repeat the command sir ?");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (respond.contains("at ")) {
                String timeContainsText = respond.split("at ")[1];
                int hour = -1, min = -1;
                String hourString, minuteString;
                System.out.println("contain alarm at");

                if (timeContainsText.contains(":") || timeContainsText.contains("and")) {

                    if (timeContainsText.contains(":")) {
                        hourString = timeContainsText.split(":")[0];
                        minuteString = timeContainsText.split(":")[1];
                    } else {
                        hourString = timeContainsText.split("and")[0];
                        minuteString = timeContainsText.split("and")[1];

                    }
                    for (String time : hourString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);

                        }
                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {

                            if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                if (number < 12) {
                                    number += 12;
                                } else if (number == 12) {
                                    number = 0;
                                }
                            }
                            hour = number;
                        }

                    }
                    for (String time : minuteString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);
                        }
                        if (time.contains("quarter")) {
                            number = 15;
                        } else if (time.contains("half")) {
                            number = 30;
                        }

                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {
                            min = number;
                        }

                    }

                } else if (timeContainsText.replaceAll(" ", "").contains("oclock") || timeContainsText.replaceAll(" ", "").contains("o'clock")) {

                    hourString = timeContainsText.split("oclock")[0];
                    minuteString = "0";

                    for (String time : hourString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);

                        }
                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {

                            if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                if (number < 12) {
                                    number += 12;
                                } else if (number == 12) {
                                    number = 0;
                                }
                            }
                            hour = number;
                        }

                    }
                    min = 0;

                } else if (timeContainsText.contains(" past ")) {

                    System.out.println("contain alarm past");

                    hourString = timeContainsText.split(" past ")[1];
                    minuteString = timeContainsText.split(" past ")[0];

                    for (String time : hourString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);

                        }
                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {

                            if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                if (number < 12) {
                                    number += 12;
                                } else if (number == 12) {
                                    number = 0;
                                }
                            }
                            hour = number;
                        }

                    }
                    for (String time : minuteString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);
                        }
                        if (time.contains("quarter")) {
                            number = 15;
                        } else if (time.contains("half")) {
                            number = 30;
                        }

                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {
                            min = number;
                        }

                    }

                } else if (timeContainsText.contains(" to ")) {

                    System.out.println("contain alarm past");

                    hourString = timeContainsText.split(" to ")[1];
                    minuteString = timeContainsText.split(" to ")[0];

                    for (String time : hourString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);

                        }
                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {
                            number--;
                            if (respond.contains("post meridiem") || respond.contains(" pm") || respond.contains("p.m.")) {
                                if (number < 12) {
                                    number += 12;
                                } else if (number == 12) {
                                    number = 0;
                                }
                            }
                            hour = number;
                        }
                        break;

                    }
                    for (String time : minuteString.split(" ")) {

//                            String time = timeContainsText.split(" ")[0];
                        int number = 0;
                        try {
                            number = Integer.parseInt(time);
                        } catch (Exception e) {
                            number = inNumerals(time);
                        }
                        if (time.contains("quarter")) {
                            number = 15;
                        } else if (time.contains("half")) {
                            number = 30;
                        }

                        if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                            continue;
                        } else {
                            min = 60 - number;

                        }
                        break;

                    }

                }

                System.out.println("hour " + hour);
                System.out.println("min " + min);

                if (hour != -1 && min != -1) {

                    alarmAtTime(hour, min);
                }
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name)
                    && (respond.replaceAll(" ", "").contains("off")
                    || respond.replaceAll(" ", "").contains("close")
                    || respond.replaceAll(" ", "").contains("disable")
                    || respond.replaceAll(" ", "").contains("deactivate"))
                    && (respond.replaceAll(" ", "").contains("all")
                    || respond.replaceAll(" ", "").contains("everything"))) {

                if (respond.contains("except")) {
                    if (respond.contains("in ") || respond.contains("time")) {
                        try {
                            String timeContainsText = null;
                            if (respond.contains("in ")) {
                                timeContainsText = respond.split("in ")[1];
                            } else if (respond.contains("time")) {
                                timeContainsText = respond.split("time")[1];

                            }
                            if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                dospeak("can you repeat the command sir?");
                                System.out.println("repeat command");
                                return;
                            }

                            boolean found = false;
                            String[] wordsList = timeContainsText.split(" ");
                            for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                                String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                int number = 0;
                                try {
                                    number = Integer.parseInt(time);
                                } catch (Exception e) {
                                    number = inNumerals(time);

                                }
                                if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                    continue;
                                }
                                found = true;
                                if (timeUnit.contains("minute")) {
                                    if (number > 1) {
                                        timeUnit = "minutes";
                                    } else {
                                        timeUnit = "minute";
                                    }
                                } else if (timeUnit.contains("sec")) {
                                    if (number > 1) {
                                        timeUnit = "seconds";
                                    } else {
                                        timeUnit = "second";
                                    }
                                } else if (timeUnit.contains("hour")) {
                                    if (number > 1) {
                                        timeUnit = "hours";
                                    } else {
                                        timeUnit = "hour";
                                    }
                                } else if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }

                                String cmd = respond.split("except")[1];
                                for (String str : SH.outputPowerCommands) {
                                    if (cmd.contains(str)) {
                                        turnOffAllDeviceExeptInNTime(str, number, timeUnit);
                                        break;
                                    }
                                }
                            }
                            if (!found) {
                                System.out.println("Can you repeat the command sir ?");
                                dospeak("Can you repeat the command sir ?");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        closeAllExcept(respond.split("except")[1]);
                    }

                } else if (respond.contains("in ") || respond.contains("time")) {
                    try {
                        String timeContainsText = null;
                        if (respond.contains("in ")) {
                            timeContainsText = respond.split("in ")[1];
                        } else if (respond.contains("time")) {
                            timeContainsText = respond.split("time")[1];

                        }
                        if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                            dospeak("can you repeat the command sir?");
                            System.out.println("repeat command");
                            return;
                        }

                        boolean found = false;
                        String[] wordsList = timeContainsText.split(" ");
                        for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                            String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            }
                            found = true;
                            if (timeUnit.contains("minute")) {
                                if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }
                            } else if (timeUnit.contains("sec")) {
                                if (number > 1) {
                                    timeUnit = "seconds";
                                } else {
                                    timeUnit = "second";
                                }
                            } else if (timeUnit.contains("hour")) {
                                if (number > 1) {
                                    timeUnit = "hours";
                                } else {
                                    timeUnit = "hour";
                                }
                            } else if (number > 1) {
                                timeUnit = "minutes";
                            } else {
                                timeUnit = "minute";
                            }

                            turnOffAllDeviceInNTime(number, timeUnit);
                        }
                        if (!found) {
                            System.out.println("Can you repeat the command sir ?");

                            dospeak("Can you repeat the command sir ?");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    closeAll();
                }
                dospeak("right away sir.");
                respond = "";
                lastProcessRespond = "";

            } else if (respond.replaceAll(
                    " ", "").contains(name)
                    && (respond.replaceAll(" ", "").contains("on")
                    || respond.replaceAll(" ", "").contains("open")
                    || respond.replaceAll(" ", "").contains("enable")
                    || respond.replaceAll(" ", "").contains("activate"))
                    && (respond.replaceAll(" ", "").contains("all")
                    || respond.replaceAll(" ", "").contains("everything"))) {

                if (respond.contains("except")) {
                    if (respond.contains("in ") || respond.contains("time")) {
                        try {
                            String timeContainsText = null;
                            if (respond.contains("in ")) {
                                timeContainsText = respond.split("in ")[1];
                            } else if (respond.contains("time")) {
                                timeContainsText = respond.split("time")[1];

                            }
                            if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                dospeak("can you repeat the command sir?");
                                System.out.println("repeat command");
                                return;
                            }

                            boolean found = false;
                            String[] wordsList = timeContainsText.split(" ");
                            for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                                String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                int number = 0;
                                try {
                                    number = Integer.parseInt(time);
                                } catch (Exception e) {
                                    number = inNumerals(time);

                                }
                                if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                    continue;
                                }
                                found = true;
                                if (timeUnit.contains("minute")) {
                                    if (number > 1) {
                                        timeUnit = "minutes";
                                    } else {
                                        timeUnit = "minute";
                                    }
                                } else if (timeUnit.contains("sec")) {
                                    if (number > 1) {
                                        timeUnit = "seconds";
                                    } else {
                                        timeUnit = "second";
                                    }
                                } else if (timeUnit.contains("hour")) {
                                    if (number > 1) {
                                        timeUnit = "hours";
                                    } else {
                                        timeUnit = "hour";
                                    }
                                } else if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }

                                String cmd = respond.split("except")[1];
                                for (String str : SH.outputPowerCommands) {
                                    if (cmd.contains(str)) {
                                        turnOnAllDeviceExeptInNTime(str, number, timeUnit);
                                        break;
                                    }
                                }
                            }
                            if (!found) {
                                System.out.println("Can you repeat the command sir ?");

                                dospeak("Can you repeat the command sir ?");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        openAllExcept(respond.split("except")[1]);
                    }

                } else if (respond.contains("in ") || respond.contains("time")) {
                    try {
                        String timeContainsText = null;
                        if (respond.contains("in ")) {
                            timeContainsText = respond.split("in ")[1];
                        } else if (respond.contains("time")) {
                            timeContainsText = respond.split("time")[1];

                        }
                        if (timeContainsText == null) {
                            return;
                        }

                        boolean found = false;
                        String[] wordsList = timeContainsText.split(" ");
                        for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                            String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                            int number = 0;
                            try {
                                number = Integer.parseInt(time);
                            } catch (Exception e) {
                                number = inNumerals(time);

                            }
                            if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                continue;
                            }
                            found = true;
                            if (timeUnit.contains("minute")) {
                                if (number > 1) {
                                    timeUnit = "minutes";
                                } else {
                                    timeUnit = "minute";
                                }
                            } else if (timeUnit.contains("sec")) {
                                if (number > 1) {
                                    timeUnit = "seconds";
                                } else {
                                    timeUnit = "second";
                                }
                            } else if (timeUnit.contains("hour")) {
                                if (number > 1) {
                                    timeUnit = "hours";
                                } else {
                                    timeUnit = "hour";
                                }
                            } else if (number > 1) {
                                timeUnit = "minutes";
                            } else {
                                timeUnit = "minute";
                            }

                            turnOnAllDeviceInNTime(number, timeUnit);
                        }
                        if (!found) {
                            System.out.println("Can you repeat the command sir ?");

                            dospeak("Can you repeat the command sir ?");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    openAll();
                }
                dospeak("right away sir.");
                respond = "";
                lastProcessRespond = "";

            } else if (respond.replaceAll(" ", "").contains(name)
                    && (respond.replaceAll(" ", "").contains("on")
                    || respond.replaceAll(" ", "").contains("open")
                    || respond.replaceAll(" ", "").contains("enable")
                    || respond.replaceAll(" ", "").contains("activate"))) {

                for (String outCommand : SH.outputPowerCommands) {
                    if (respond.contains(outCommand)) {
                        if (respond.contains("in ") || respond.contains("time")) {
                            try {
                                String timeContainsText = null;
                                if (respond.contains("in ")) {
                                    timeContainsText = respond.split("in ")[1];
                                } else if (respond.contains("time")) {
                                    timeContainsText = respond.split("time")[1];

                                }
                                if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                    dospeak("can you repeat the command sir?");
                                    System.out.println("repeat command");
                                    return;
                                }

                                boolean found = false;
                                String[] wordsList = timeContainsText.split(" ");
                                for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                                    String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                    int number = 0;
                                    try {
                                        number = Integer.parseInt(time);
                                    } catch (Exception e) {
                                        number = inNumerals(time);

                                    }
                                    if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                        continue;
                                    }
                                    found = true;
                                    if (timeUnit.contains("hour")) {
                                        if (number > 1) {
                                            timeUnit = "hours";
                                        } else {
                                            timeUnit = "hour";
                                        }
                                    } else if (timeUnit.contains("minute")) {
                                        if (number > 1) {
                                            timeUnit = "minutes";
                                        } else {
                                            timeUnit = "minute";
                                        }
                                    } else if (timeUnit.contains("sec")) {
                                        if (number > 1) {
                                            timeUnit = "seconds";
                                        } else {
                                            timeUnit = "second";
                                        }
                                    } else if (number > 1) {
                                        timeUnit = "minutes";
                                    } else {
                                        timeUnit = "minute";
                                    }

                                    turnOnDeviceInNTime(outCommand, number, timeUnit);
                                }
                                if (!found) {
                                    System.out.println("Can you repeat the command sir ?");

                                    dospeak("Can you repeat the command sir ?");

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            turnOnDevice(outCommand);
                        }
                    }
                }
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name)
                    && (respond.replaceAll(" ", "").contains("off")
                    || respond.replaceAll(" ", "").contains("close")
                    || respond.replaceAll(" ", "").contains("disable")
                    || respond.replaceAll(" ", "").contains("deactivate"))) {

                for (String outCommand : SH.outputPowerCommands) {
                    if (respond.contains(outCommand)) {
                        if (respond.contains("in ") || respond.contains("time")) {
                            try {
                                String timeContainsText = null;
                                if (respond.contains("in ")) {
                                    timeContainsText = respond.split("in ")[1];
                                } else if (respond.contains("time")) {
                                    timeContainsText = respond.split("time")[1];

                                }
                                if (timeContainsText == null || timeContainsText.replaceAll(" ", "").equals("")) {
                                    dospeak("can you repeat the command sir?");
                                    System.out.println("repeat command");
                                    return;
                                }

                                boolean found = false;
                                String[] wordsList = timeContainsText.split(" ");
                                for (String time : wordsList) {

//                            String time = timeContainsText.split(" ")[0];
                                    String timeUnit = timeContainsText.substring(time.length(), timeContainsText.length());
                                    int number = 0;
                                    try {
                                        number = Integer.parseInt(time);
                                    } catch (Exception e) {
                                        number = inNumerals(time);

                                    }
                                    if (number == 0 && (!time.equals("0") || !time.equals("00") || !time.equalsIgnoreCase("zero"))) {
                                        continue;
                                    }
                                    found = true;
                                    if (timeUnit.contains("minute")) {
                                        if (number > 1) {
                                            timeUnit = "minutes";
                                        } else {
                                            timeUnit = "minute";
                                        }
                                    } else if (timeUnit.contains("sec")) {
                                        if (number > 1) {
                                            timeUnit = "seconds";
                                        } else {
                                            timeUnit = "second";
                                        }
                                    } else if (timeUnit.contains("hour")) {
                                        if (number > 1) {
                                            timeUnit = "hours";
                                        } else {
                                            timeUnit = "hour";
                                        }
                                    } else if (number > 1) {
                                        timeUnit = "minutes";
                                    } else {
                                        timeUnit = "minute";
                                    }

                                    turnOffDeviceInNTime(outCommand, number, timeUnit);
                                }
                                if (!found) {
                                    System.out.println("Can you repeat the command sir ?");

                                    dospeak("Can you repeat the command sir ?");

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            turnOffDevice(outCommand);
                        }
                    }
                }
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "hi") || respond.replaceAll(" ", "").contains(name + "hello")
                    || respond.replaceAll(" ", "").contains("hi" + name) || respond.replaceAll(" ", "").contains("hello" + name)) {
                welcome();
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "aresokind") || respond.replaceAll(" ", "").contains("aresokind" + name)) {
                dospeak("Thank you sir.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "what") && (respond.replaceAll(" ", "").contains("mission" + name)
                    || respond.replaceAll(" ", "").contains("purpose" + name) || respond.replaceAll(" ", "").contains("aim" + name)
                    || respond.replaceAll(" ", "").contains("aim" + name) || respond.replaceAll(" ", "").contains("aim" + name))) {
                dospeak("My purpose is to surve as good as I can my master.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "hi") || respond.replaceAll(" ", "").contains(name + "hello")
                    || respond.replaceAll(" ", "").contains("hi" + name) || respond.replaceAll(" ", "").contains("hello" + name)) {
                dospeak("wellcome sir");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "stop") || respond.replaceAll(" ", "").contains(name + "cancel")) {
                stopSpeak();
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "bye") || respond.replaceAll(" ", "").contains("bye" + name)
                    || respond.replaceAll(" ", "").contains(name + "goodbye") || respond.replaceAll(" ", "").contains("goodbye" + name)
                    || respond.replaceAll(" ", "").contains(name + "see you") || respond.replaceAll(" ", "").contains("see you" + name)) {

                turnEverythingOff();
                respond = "";
                lastProcessRespond = "";
            } else if ((respond.replaceAll(
                    " ", "").contains("find") || respond.replaceAll(" ", "").contains("whereis"))
                    && (respond.replaceAll(" ", "").contains("phone") || respond.replaceAll(" ", "").contains("mobile"))) {
                System.out.println("findMobile search");
                findMobile();
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "about") || respond.replaceAll(" ", "").contains("about" + name)) {
                dospeak("I am a smart house application, made by nikos Gaitanis, known as tsoglani!.My purpose is to surve my master.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains(name + "info") || respond.replaceAll(" ", "").contains("info" + name)
                    || respond.replaceAll(" ", "").contains(name + "information") || respond.replaceAll(" ", "").contains("information" + name)) {
                dospeak("I am authorized to turn on or off the electrical devices, and asnswear to some of your question master.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("clever") && respond.replaceAll(" ", "").contains("areyou")) {
                dospeak("I am as cleven as a mashine can be sir.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("clever") && (respond.replaceAll(" ", "").contains("nick") || respond.replaceAll(" ", "").contains("nikos")
                    || respond.replaceAll(" ", "").contains("tsoglani") || respond.replaceAll(" ", "").contains("master") || respond.replaceAll(" ", "").contains("boss"))) {
                dospeak("Yes, he is very clever, I could say he is a jenious.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("howoldareyou" + name) || respond.replaceAll(" ", "").contains(name + "howoldareyou")
                    || respond.replaceAll(" ", "").contains("isyourage")) {
                dospeak("I am a machine, I have no age, I am imortal.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("boss") || respond.replaceAll(" ", "").contains(name + "nick") || respond.replaceAll(" ", "").contains(name + "nikos")
                    || respond.replaceAll(" ", "").contains(name + "tsoglani") || respond.replaceAll(" ", "").contains(name + "master")
                    && (respond.replaceAll(" ", "").contains("pussy") || respond.replaceAll(" ", "").contains("gay") || respond.replaceAll(" ", "").contains("homo")
                    || respond.replaceAll(" ", "").contains("stupid") || respond.replaceAll(" ", "").contains("motherfucker") || respond.replaceAll(" ", "").contains("suck")
                    || respond.replaceAll(" ", "").contains("sucker") || respond.replaceAll(" ", "").contains("fool") || respond.replaceAll(" ", "").contains("simp")
                    || respond.replaceAll(" ", "").contains("dupe") || respond.replaceAll(" ", "").contains("simpleton") || respond.replaceAll(" ", "").contains("moron")
                    || respond.replaceAll(" ", "").contains("jackass") || respond.replaceAll(" ", "").contains("goof") || respond.replaceAll(" ", "").contains("slob")
                    || respond.replaceAll(" ", "").contains("booby") || respond.replaceAll(" ", "").contains("duffer") || respond.replaceAll(" ", "").contains("idiot"))) {

                dospeak("No he is not, beware your words for my master sir.");
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("whatisyourname") || respond.replaceAll(" ", "").contains("what'syourname")) {
                if (name.replaceAll(" ", "").equals("")) {
                    dospeak("I have no name.");
                } else {
                    dospeak("My name is " + name + " sir.");
                }
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("weatherin" + name) || respond.replaceAll(" ", "").contains(name + "weatherin")) {
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
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("weather" + name) || respond.replaceAll(" ", "").contains(name + "weather")) {

                String weatherInfo;
                if (respond.replaceAll(" ", "").contains("forecast")) {
                    weatherInfo = weather.getForecastWeatherAtCity("Ierapetra");

                } else {
                    weatherInfo = weather.getWeather("Ierapetra");
                }
                dospeak(weatherInfo);
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("whatis") && respond.replaceAll(" ", "").contains(name)
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
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("pause" + name) || respond.replaceAll(
                    " ", "").contains(name + "pause")) {
                pauseAudio();
            } else if (respond.replaceAll(
                    " ", "").contains("resume" + name) || respond.replaceAll(
                    " ", "").contains(name + "resume")) {
                resumeAudio();
            } else if (respond.replaceAll(
                    " ", "").contains("playaudio" + name)
                    || respond.replaceAll(" ", "").contains(name + "playaudio")
                    || respond.replaceAll(" ", "").contains("playsound" + name)
                    || respond.replaceAll(" ", "").contains(name + "playsound")
                    || respond.replaceAll(" ", "").contains("playmusic" + name)
                    || respond.replaceAll(" ", "").contains(name + "playmusic")
                    || respond.replaceAll(" ", "").contains("playsong" + name)
                    || respond.replaceAll(" ", "").contains(name + "playsong")) {

                dospeak("right away sir.");
                playAudio();
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("nextsong")
                    || respond.replaceAll(" ", "").contains("next")) {
                nextSong();

            } else if (respond.replaceAll(
                    " ", "").contains("previous")
                    || respond.replaceAll(" ", "").contains("previoussong")) {
                previousSong();
                respond = "";
                lastProcessRespond = "";
            } else if (respond.replaceAll(
                    " ", "").contains("howareyou" + name) || respond.replaceAll(" ", "").contains(name + "howareyou")) {
                dospeak("I am fine sir, thanks for asking. Would you like something else.");
            } else if (!respond.contains(respond)) {
                respond += " " + respond;
            }
        }
    }

    private void findMobile() {
        try {
            dospeak("right away sir.");
            if (findMobileProcess != null && findMobileProcess.isAlive()) {
                findMobileProcess.destroy();
            }
            findMobileProcess = Runtime.getRuntime().exec("java -jar " + findMobileScr);

        } catch (IOException ex) {
            Logger.getLogger(Jarvis.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();

        String dateString = dateFormat.format(date).toString().split(" ")[0];

        String output = "";
        String[] numList = dateString.split("/");
        for (int i = numList.length - 1; i >= 0; i--) {
            if (i == 0) {
                output += " " + numList[i];
            } else if (i == 1) {
                try {
                    output += " of " + getMonthForInt(Integer.parseInt(numList[i]));
                } catch (Exception e) {
                    output += Integer.parseInt(numList[i]);

                }
            } else if (i == 2) {

                output += numList[i];
            }
        }
        System.out.println(output);
        return output;//2014/08/06 16:06:54
    }

    private String getStringDate() {

        Calendar cal = Calendar.getInstance();
        String dayOfWeek = getDayOfWeek(cal.DAY_OF_WEEK);
        return dayOfWeek;
    }

    private String getCurentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        return (dateFormat.format(date).toString().split(" ")[1]);//2014/08/06 16:06:54
    }

    boolean hasAlarm = false;
    Thread alarmThread;
    int timeCounter;

    private void alarmAtTime(int hour, int min) {

        try {
            System.out.println("activate alarm");

            String fullTime = "";
            if (hour < 10) {
                fullTime += "0";
            }
            fullTime += hour + ":";
            if (min < 10) {
                fullTime += "0";
            }
            fullTime += min + ":00";

            Calendar cal = Calendar.getInstance();
            int curentHour = cal.get(cal.HOUR_OF_DAY);
            int curentMin = cal.get(cal.MINUTE);
            int curentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

//          Calendar c = Calendar.getInstance(); 
//            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//	   //get current date time with Date()
//	   Date date = new Date();
//	   System.out.println(dateFormat.format(date));
//	  
//	   //get current date time with Calendar()
//	  dateFormat.format(cal.getTime());
//c.setTime(date); 
//c.add(Calendar.DATE, 1);
//date = c.getTime();
            Date targetDateAlarm;
            DateTime usingTimeDate;
            String extraInfo = "today";
            if (curentHour > hour || curentHour == hour && curentMin >= min) {
                System.out.println("tomorrow date");
                extraInfo = "tomorrow";
//                org.joda.time.DateTimeZone timeZone = org.joda.time.DateTimeZone.forID("America/Los_Angeles");
                DateTime now = new DateTime();
                usingTimeDate = now.plusDays(1);

//                int year = tomorrowAsJUDate.getYear();
//                int month = tomorrowAsJUDate.getMonth();
//                int day = tomorrowAsJUDate.getDate();
//                System.out.println(year + "/" + month + "/" + day);
//                targetDateAlarm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(year + "/" + month + "/" + day + " " + fullTime);
//                System.out.println(targetDateAlarm);
            } else {
                usingTimeDate = new DateTime();
//                targetDateAlarm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(fullTime);
            }

            targetDateAlarm = usingTimeDate.toDate();
            targetDateAlarm.setHours(hour);
            targetDateAlarm.setMinutes(min);
            targetDateAlarm.setSeconds(0);
            System.out.println(targetDateAlarm);
            DateTime now = new DateTime();

            long diffInMillis = (targetDateAlarm.getTime() - now.toDate().getTime()) / 1000;
            System.out.println(diffInMillis + " seconds");
            dospeak("alarm will ring at " + hour + " and " + min + " o'clock " + extraInfo + " sir.");

            alarmInNTime((int) diffInMillis, "seconds", false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void alarmInNTime(int time, String type, boolean isGoingToTalk) {
        if (isGoingToTalk) {
            dospeak("alarm in " + time + " " + type);
        }
        System.out.println("alarm in ");
        if (type.startsWith("minute")) {
            time *= 60;
        } else if (type.startsWith("hour")) {
            time *= 60 * 60;
        }
        timeCounter = time;
        if (alarmThread == null) {
            alarmThread = new Thread() {
                @Override
                public void run() {
                    hasAlarm = true;

                    while (hasAlarm) {
                        try {
                            sleep(1000);
                            timeCounter--;
                            System.out.println(timeCounter);

                            if (timeCounter <= 0) {
                                hasAlarm = false;
                                chooseRandomSong();
                                System.out.println("play");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Jarvis.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    alarmThread = null;
                    hasAlarm = false;

                }

            };
            alarmThread.start();
        }

    }

    private void chooseRandomSong() {
        if (mp3Player != null && !mp3Player.isStopped()) {
            mp3Player.stop();
        }

        mp3Player = new MP3Player();

        String[] files = musicFile.list();
        boolean notFound = true;
        ArrayList<File> foundSongs = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            String title = files[i];
            File cf = new File(musicFilesLocation + "/" + title);
            if (cf.isFile()) {
//                mp3Player.addToPlayList(cf);
                foundSongs.add(cf);
            }

        }
        long seed = System.nanoTime();
        Collections.shuffle(foundSongs, new Random(seed));
        mp3Player.addToPlayList(foundSongs.get(0));
        mp3Player.play();

    }

    private void turnOnDevice(String device) {//// implement gpiopins
        System.out.println(device + " turned to on");
        dospeak(device + " turned to on");
    }

    private void turnOnDeviceInNTime(String device, int time, String timeUnit) {//// implement gpiopins
        System.out.println(device + " will turn on in " + time + " " + timeUnit + ".");
        dospeak(device + " will turn on in " + time + " " + timeUnit + ".");
    }

    private void turnOffDeviceInNTime(String device, int time, String timeUnit) {//// implement gpiopins
        System.out.println(device + " will turn off in " + time + " " + timeUnit + ".");
        dospeak(device + " will turn off in " + time + " " + timeUnit + ".");
    }

    private void closeAll() {//// implement gpiopins
        System.out.println("close all");

    }

    private void openAll() {//// implement gpiopins
        System.out.println("open all");

    }

    private void turnOffAllDeviceExeptInNTime(String deviceNotToClose, int time, String timeUnit) {//// implement gpiopins
        System.out.println("close all devices except " + deviceNotToClose + " in " + time + " " + timeUnit);

    }

    private void turnOnAllDeviceExeptInNTime(String deviceNotToClose, int time, String timeUnit) {//// implement gpiopins
        System.out.println("open all devices except " + deviceNotToClose + " in " + time + " " + timeUnit);
    }

    private void turnOffAllDeviceInNTime(int time, String timeUnit) {//// implement gpiopins
        System.out.println("close all in " + time + " " + timeUnit);

    }

    private void turnOnAllDeviceInNTime(int time, String timeUnit) {//// implement gpiopins
        System.out.println("open all in " + time + " " + timeUnit);

    }

    private void closeAllExcept(String commandNotClose) {//// implement gpiopins
        System.out.println("close all except " + commandNotClose);

    }

    private void openAllExcept(String commandNotClose) {//// implement gpiopins
        System.out.println("open all except " + commandNotClose);
    }

    private void turnOffDevice(String device) {//// implement gpiopins

        System.out.println(device + " turned to off");
        dospeak(device + " turned to off");
    }

    private void welcome() {//// implement gpiopins and turn livingroom lights on
        dospeak("welcome sir");
        // turn all the lights off
    }

    private void turnEverythingOff() {
        dospeak("bye sir");
        closeAll();
        // turn all the lights off
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
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
        if (speechProcess != null) {
            speechProcess.destroyForcibly();

        }
        if (speechProcess != null) {
            speechProcess.destroy();

        }

        if (mp3Player != null) {
            mp3Player.stop();
        }
        hasAlarm = false;

    }

    public void close() {
        try {
            stopSpeak();
            if (mic != null) {
                mic.close();
            }

            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

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

            if (speechProcess != null) {
                speechProcess.destroyForcibly();
            }

////sudo apt-get install espeak
            speechProcess = Runtime.getRuntime().exec(new String[]{"espeak", '\"' + speaktext + '\"'});

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
                if (gSpeechListener != null) {
                    duplex.removeResponseListener(gSpeechListener);
                }
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

    private void pauseAudio() {
        if (mp3Player != null && !mp3Player.isPaused() && !mp3Player.isStopped()) {
            mp3Player.pause();
        }
    }

    private void resumeAudio() {
        if (mp3Player != null && !mp3Player.isPaused()) {
            mp3Player.play();
        }
    }

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
            Logger.getLogger(Jarvis.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    public int inNumerals(String inwords) {

        int wordnum = 0;
        String[] arrinwords = inwords.split(" ");
        int arrinwordsLength = arrinwords.length;
        if (inwords.equals("zero")) {
            return 0;
        }
        if (inwords.contains("thousand")) {
            int indexofthousand = inwords.indexOf("thousand");
            //System.out.println(indexofthousand);
            String beforethousand = inwords.substring(0, indexofthousand);
            //System.out.println(beforethousand);
            String[] arrbeforethousand = beforethousand.split(" ");
            int arrbeforethousandLength = arrbeforethousand.length;
            //System.out.println(arrbeforethousandLength);
            if (arrbeforethousandLength == 2) {
                wordnum = wordnum + 1000 * (wordtonum(arrbeforethousand[0]) + wordtonum(arrbeforethousand[1]));
                //System.out.println(wordnum);
            }
            if (arrbeforethousandLength == 1) {
                wordnum = wordnum + 1000 * (wordtonum(arrbeforethousand[0]));
                //System.out.println(wordnum);
            }

        }
        if (inwords.contains("hundred")) {
            int indexofhundred = inwords.indexOf("hundred");
            //System.out.println(indexofhundred);
            String beforehundred = inwords.substring(0, indexofhundred);

            //System.out.println(beforehundred);
            String[] arrbeforehundred = beforehundred.split(" ");
            int arrbeforehundredLength = arrbeforehundred.length;
            wordnum = wordnum + 100 * (wordtonum(arrbeforehundred[arrbeforehundredLength - 1]));
            String afterhundred = inwords.substring(indexofhundred + 8);//7 for 7 char of hundred and 1 space
            //System.out.println(afterhundred);
            String[] arrafterhundred = afterhundred.split(" ");
            int arrafterhundredLength = arrafterhundred.length;
            if (arrafterhundredLength == 1) {
                wordnum = wordnum + (wordtonum(arrafterhundred[0]));
            }
            if (arrafterhundredLength == 2) {
                wordnum = wordnum + (wordtonum(arrafterhundred[1]) + wordtonum(arrafterhundred[0]));
            }
            //System.out.println(wordnum);

        }
        if (!inwords.contains("thousand") && !inwords.contains("hundred")) {
            if (arrinwordsLength == 1) {
                wordnum = wordnum + (wordtonum(arrinwords[0]));
            }
            if (arrinwordsLength == 2) {
                wordnum = wordnum + (wordtonum(arrinwords[1]) + wordtonum(arrinwords[0]));
            }
            //System.out.println(wordnum);
        }

        return wordnum;
    }

    public int wordtonum(String word) {
        int num = 0;
        switch (word) {
            case "one":
                num = 1;
                break;
            case "two":
                num = 2;
                break;
            case "three":
                num = 3;
                break;
            case "four":
                num = 4;
                break;
            case "five":
                num = 5;
                break;
            case "six":
                num = 6;
                break;
            case "seven":
                num = 7;
                break;
            case "eight":
                num = 8;
                break;
            case "nine":
                num = 9;
                break;
            case "ten":
                num = 10;
                break;
            case "eleven":
                num = 11;
                break;
            case "twelve":
                num = 12;
                break;
            case "thirteen":
                num = 13;
                break;
            case "fourteen":
                num = 14;
                break;
            case "fifteen":
                num = 15;
                break;
            case "sixteen":
                num = 16;
                break;
            case "seventeen":
                num = 17;
                break;
            case "eighteen":
                num = 18;
                break;
            case "nineteen":
                num = 19;
                break;
            case "twenty":
                num = 20;
                break;
            case "thirty":
                num = 30;
                break;
            case "forty":
                num = 40;
                break;
            case "fifty":
                num = 50;
                break;
            case "sixty":
                num = 60;
                break;
            case "seventy":
                num = 70;
                break;
            case "eighty":
                num = 80;
                break;
            case "ninety":
                num = 90;
                break;
            case "hundred":
                num = 100;
                break;
            case "thousand":
                num = 1000;
                break;
            /*default: num = "Invalid month";
                             break;*/
        }
        return num;
    }

    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private String getDateAndTimeIn(String city) {

        return "Date is " + getDateIN(city, false) + ", curent time is " + getTimeIn(city, false) + " sir.";
    }

    private String getTimeIn(String city, boolean extraInfo) {
        ZonedDateTime zoneDateTime = null;
        for (int i = 0; i < CodeList.length; i++) {
            try {
                String code = CodeList[0];

                java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
                ZoneId zone = ZoneId.of(code + "/" + city);
                System.out.println("TimeZone : " + zone);

                //LocalDateTime + ZoneId = ZonedDateTime
                zoneDateTime = ldt.atZone(zone);
            } catch (Exception e) {
//                e.printStackTrace();
                continue;
            }
            break;
        }
//        ZoneId newYokZoneId = ZoneId.of("America/New_York");
//        System.out.println("TimeZone : " + newYokZoneId);
//
//        ZonedDateTime nyDateTime = zoneDateTime.withZoneSameInstant(newYokZoneId);
//        System.out.println("Date (New York) : " + nyDateTime);
//
        DateTimeFormatter format = DateTimeFormatter.ofPattern(TIME_FORMAT);
//        System.out.println("\n---DateTimeFormatter---");
//        System.out.println("Date (Singapore) : " + format.format(zoneDateTime));
//        System.out.println("Date (New York) : " + format.format(nyDateTime));
//        System.out.println("Date (Singapore) : " + );
        if (zoneDateTime == null || format == null) {
            return null;
        }
        String output = format.format(zoneDateTime);

        if (extraInfo) {
            output = "Time in " + city + " is " + output + " sir.";
        }
        System.out.println("Time in " + city + " is " + output);

        return output;
    }

    private String[] CodeList = {
        "Europe", "Asia", "Pacific", "America", "Atlantic", "Africa", "Antarctica", "Indian", "Australia", "Etc"
    };

    private String getDateIN(String city, boolean extraInfo) {

//       DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        //get current date time with Date()
        ZonedDateTime zoneDateTime = null;
        for (int i = 0; i < CodeList.length; i++) {
            try {
                String code = CodeList[0];

                java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
                ZoneId zone = ZoneId.of(code + "/" + city);
                System.out.println("TimeZone : " + zone);

                //LocalDateTime + ZoneId = ZonedDateTime
                zoneDateTime = ldt.atZone(zone);
                System.out.println("Date in " + city + " is " + zoneDateTime);
            } catch (Exception e) {
//                e.printStackTrace();
                continue;
            }
            break;
        }
//        ZoneId newYokZoneId = ZoneId.of("America/New_York");
//        System.out.println("TimeZone : " + newYokZoneId);
//
//        ZonedDateTime nyDateTime = zoneDateTime.withZoneSameInstant(newYokZoneId);
//        System.out.println("Date (New York) : " + nyDateTime);
//
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
//        System.out.println("\n---DateTimeFormatter---");
//        System.out.println("Date (Singapore) : " + format.format(zoneDateTime));
//        System.out.println("Date (New York) : " + format.format(nyDateTime));
//        System.out.println("Date (Singapore) : " + );
        if (zoneDateTime == null || format == null) {
            return null;
        }

        String dateString = format.format(zoneDateTime).split(" ")[0];

        String output="";

        String[] numList = dateString.split("/");
        for (int i = numList.length - 1; i >= 0; i--) {
            if (i == 0) {
                output += " " + numList[i];
            } else if (i == 1) {
                try {
                    output += " of " + getMonthForInt(Integer.parseInt(numList[i]));
                } catch (Exception e) {
                    output += Integer.parseInt(numList[i]);

                }
            } else if (i == 2) {

                output += numList[i];
            }
        }
        System.out.println(output);


        if (extraInfo) {
            output = "Date in " + city + " is " + output + " sir.";
        }
        return output;
    }

}
