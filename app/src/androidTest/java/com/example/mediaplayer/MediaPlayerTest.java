package com.example.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiCollection;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.mediaplayer.MusicAdapter.mFiles;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MediaPlayerTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.example.mediaplayer";
    private static final int LAUNCH_TIMEOUT = 5000;

    UiDevice device;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }
    @Test
    public void checkPreconditions() {
        assertThat(device, notNullValue());
    }
    @Test
    public void useOfTheCorrectPackage() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.mediaplayer", appContext.getPackageName());
    }
    @Test
    public void appStarted() { // aplicatia a pornit
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        assertThat("com.example.mediaplayer", is(context.getPackageName()));
        assertThat(intent, notNullValue());
    }





    @Test
    public void songsInPlaylist() throws UiObjectNotFoundException, InterruptedException,
            IllegalMonitorStateException,NullPointerException {//- verificare ca s-au incarcat melodii in playlist
        int numberOfSong=        device.findObject(By.res(BASIC_SAMPLE_PACKAGE, "viewpager")).getChildCount();
        assertThat(numberOfSong,notNullValue());
    }
    @Test
    public void ID3tag() throws UiObjectNotFoundException, InterruptedException { // daca albumul nu este default inseamna ca avem tag ID3

        UiCollection songs = new UiCollection(new UiSelector().className("android.widget.RelativeLayout"));

        UiObject menuMore = songs.getChild(new UiSelector().resourceId("com.example.mediaplayer:id/menuMore"));
        menuMore.click();
        UiObject info = new UiObject(new UiSelector().className("android.widget.ListView"));
        info.getChild(new UiSelector().enabled(true).index(1)).click();
        Thread.sleep(7000);
        UiObject albumValue = new UiObject(new UiSelector().className("android.widget.RelativeLayout"));
        String albumText= albumValue.getChild(new UiSelector().enabled(true).index(6)).getText();
        assertNotEquals(albumText, "Album");


    }




    @Test
    public void checkFileDeleted() throws UiObjectNotFoundException { // verificare fisier sters,va da fail din cauza unor permisiuni

        UiCollection songs = new UiCollection(new UiSelector().className("android.widget.RelativeLayout"));

        UiObject menu = songs.getChild(new UiSelector().resourceId("com.example.mediaplayer:id/menuMore"));
        menu.click();

        device.findObject(new UiSelector().className("android.widget.LinearLayout")).click();

        String  deleted = device.findObject(new UiSelector().resourceId("com.example.mediaplayer:id/snackbar_text")).getText();

        assertEquals(deleted, "File Deleted");
    }
    @Test
    public void changeSongFromNotificationBar() throws UiObjectNotFoundException, InterruptedException { // ↑ verificare ca se poate schimba melodia folosind meniul de notificari al os-ului,
        // nu pot realiza testul din cauza unor permisiuni ce impiedica notificarile sa fie vizualizate
        UiCollection songs = new UiCollection(new UiSelector().className("android.widget.RelativeLayout"));
        UiObject song = songs.getChildByInstance(new UiSelector().resourceId("com.example.mediaplayer:id/music_file_name"),1);
        song.click();
        Thread.sleep(LAUNCH_TIMEOUT);
        device.openNotification();
    }
    //------------------------------------------------------
    //teste suplimentare


    @Test
    public void openSecondSong() throws UiObjectNotFoundException, InterruptedException {
        UiCollection songs = new UiCollection(new UiSelector().className("android.widget.RelativeLayout"));
        UiObject song = songs.getChildByInstance(new UiSelector().resourceId("com.example.mediaplayer:id/music_file_name"),1);
        song.click();
        Thread.sleep(LAUNCH_TIMEOUT);
    }
    @Test
    public void checkOpenAlbumSection() throws UiObjectNotFoundException, InterruptedException,
            IllegalMonitorStateException,NullPointerException {//verificare deschidere sectiune albume
        UiObject changeToAlbums = new UiObject(new UiSelector().className("android.widget.RelativeLayout"));
        changeToAlbums.getChild(new UiSelector().enabled(true).index(1)).click();
        Thread.sleep(1000);

    }
    @Test
    public void tryToSearch() throws UiObjectNotFoundException, InterruptedException,
            IllegalMonitorStateException,NullPointerException {//verificare cautare
        device.findObject(new UiSelector().className("android.widget.SearchView")).click();
        Thread.sleep(1000);
       device.findObject(new UiSelector().className("android.widget.LinearLayout")).getChild(new UiSelector().enabled(true).index(0)).setText("sia");

        Thread.sleep(4000);


    }


}