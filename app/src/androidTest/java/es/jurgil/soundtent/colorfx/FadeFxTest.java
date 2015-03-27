package com.momus.SoundTent.colorfx;

import android.graphics.Color;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;


@RunWith(RobolectricTestRunner.class)
public class FadeFxTest {
    private static final int HIGH_AMP = 25000;
    private static final int MID_AMP = 1100;
    private static final int LOW_AMP = 500;
    private static final int MAX_AMP = 32767;
    private static final int MAX_COLOR = 255;

    @Test
    public void shouldGetNewColorFromSingleAmplitude() {
        int expectedColor = Color.rgb(0, MID_AMP / FadeFx.BIT_PER_AMPLITUDE, 0);

        int newColor = new FadeFx().process(MID_AMP);

        assertThat(newColor).isEqualTo(expectedColor);
    }

    @Test
    public void shouldGetNewBrighterColorFromLargeAmplitude() {
        FadeFx fadeFx = new FadeFx();

        fadeFx.process(LOW_AMP);
        int newColor = fadeFx.process(MID_AMP);

        int expectedBrighterColor = Color.rgb(0, MID_AMP / FadeFx.BIT_PER_AMPLITUDE, 0);
        assertThat(newColor).isEqualTo(expectedBrighterColor);
    }

    @Test
    public void shouldGetNewColorFromFadingAmplitude() {
        FadeFx fader = new FadeFx();

        fader.process(HIGH_AMP);
        int newColor = fader.process(LOW_AMP);

        int expectedDarkerColor = Color.rgb(0, Math.round(HIGH_AMP * FadeFx.FADE_RATE / FadeFx.BIT_PER_AMPLITUDE), 0);
        assertThat(newColor).isEqualTo(expectedDarkerColor);
    }

    @Test
    public void shouldNotCalculateColorHigherThanColorRange() {
        FadeFx fadeFx = new FadeFx();

        int newColor = fadeFx.process(MAX_AMP);

        int expectedMaxColor = Color.rgb(0, MAX_COLOR, 0);
        assertThat(newColor).isEqualTo(expectedMaxColor);
    }

    @Test
    public void shouldNotAffectFadeIfAmplitudeIsLowerThanCurrentAmplitude() {
        FadeFx fadeFx = new FadeFx();

        fadeFx.process(HIGH_AMP);
        fadeFx.process(MID_AMP);
        int newColor = fadeFx.process(LOW_AMP);

        int expectedFadedColor = Color.rgb(0, Math.round(HIGH_AMP / FadeFx.BIT_PER_AMPLITUDE * FadeFx.FADE_RATE * FadeFx.FADE_RATE), 0);
        assertThat(newColor).isEqualTo(expectedFadedColor);
    }
}
