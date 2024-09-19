package nz.ac.auckland.se206.speech;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

/** Text-to-speech API using the JavaX speech library. */
public class FreeTextToSpeech {
  /** Custom unchecked exception for Text-to-speech issues. */
  static class TextToSpeechException extends RuntimeException {
    public TextToSpeechException(final String message) {
      super(message);
    }
  }

  /** The synthesizer used to generate speech. */
  private static final Synthesizer synthesizer;
  static {
    try {
      System.setProperty(
          "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
      Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

      synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(java.util.Locale.ENGLISH));

      synthesizer.allocate();
    } catch (final EngineException e) {
      throw new TextToSpeechException(e.getMessage());
    }
  }

  /**
   * Speaks the given sentence in input.
   *
   * @param text A string to speak.
   */
  public static void speak(final String text) {
    if (text == null) {
      throw new IllegalArgumentException("Text cannot be null.");
    }

    new Thread(
        () -> {
          try {
            synthesizer.resume();
            synthesizer.speakPlainText(text, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
          } catch (final AudioException | InterruptedException e) {
            throw new TextToSpeechException(e.getMessage());
          }
        })
        .start();
  }

  /**
   * It deallocates the speech synthesizer. If you are experiencing an
   * IllegalThreadStateException,
   * avoid using this method and run the speak method without terminating.
   */
  public static void deallocateSynthesizer() {
    try {
      synthesizer.deallocate();
    } catch (final EngineException e) {
      throw new TextToSpeechException(e.getMessage());
    }
  }
}
