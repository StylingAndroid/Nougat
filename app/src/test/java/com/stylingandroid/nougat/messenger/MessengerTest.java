package com.stylingandroid.nougat.messenger;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.stylingandroid.nougat.R.array.phrases;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessengerTest {
    private static final int ITERATIONS = 10000;
    private static final String[] PHRASES = new String[]{
            "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX"
    };

    private Messenger messenger;

    @Mock
    private Context context;

    @Mock
    private Resources resources;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(anyInt())).thenReturn(PHRASES);
        messenger = Messenger.newInstance(context);
    }

    @Test
    public void givenASetOfPhrases_whenWeRepeatedlyCallGetRandomPhrase_thenAllPhrasesAreReturnedAtSomePoint() throws Exception {
        List<String> unmatchedPhrases = new ArrayList<>(Arrays.asList(PHRASES));
        for (int i = 0; i < ITERATIONS; i++) {
            String phrase = messenger.getRandomPhrase();
            unmatchedPhrases.remove(phrase);
            if (unmatchedPhrases.isEmpty()) {
                break;
            }
        }

        assertThat(unmatchedPhrases).isEmpty();
    }

    @Test
    public void givenAValidContext_whenWeInstantiateANewMessenger_thenTheCorrectPhraseResourceIsLoaded() {
        verify(resources).getStringArray(integerArgumentCaptor.capture());

        assertThat(integerArgumentCaptor.getValue()).isEqualTo(phrases);
    }

    @Test
    public void givenAValidMessenger_whenWeGenerateANewMessage_thenTheSenderIsCorrect() {
        Message message = messenger.generateNewMessage();

        assertThat(message.sender()).isEqualTo(Messenger.SENDER);
    }

    @Test
    public void givenAValidMessenger_whenWeGenerateANewMessage_thenTheMessageIsOneOfThePhrases() {
        List<String> phrases = Arrays.asList(PHRASES);

        Message message = messenger.generateNewMessage();

        assertThat(phrases).contains(message.message());
    }

    @Test
    public void givenAValidMessenger_whenWeGenerateANewMessage_thenTheTimestampWithinTheCorrectRange() {
        long before = System.currentTimeMillis();
        Message message = messenger.generateNewMessage();
        long after = System.currentTimeMillis();

        assertThat(message.timestamp()).isBetween(before, after);
    }
}
