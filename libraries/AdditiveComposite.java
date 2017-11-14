package libraries;
import java.awt.*;
import java.awt.image.*;
import java.util.Objects;

public class AdditiveComposite implements Composite {
    private final Color chromaKey;

    public AdditiveComposite(final Color chromaKey) {
        this.chromaKey = Objects.requireNonNull(chromaKey);
    }

    public CompositeContext createContext(ColorModel srcColorModel,
            ColorModel dstColorModel, RenderingHints hints) {
        return new AdditiveCompositeContext(chromaKey);
    }
}