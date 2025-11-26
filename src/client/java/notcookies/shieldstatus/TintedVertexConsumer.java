package notcookies.shieldstatus;

import net.minecraft.client.render.VertexConsumer;

public class TintedVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final float red, green, blue, alpha;

    public TintedVertexConsumer(VertexConsumer delegate, float red, float green, float blue, float alpha) {
        this.delegate = delegate;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        return delegate.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        int tintedR = (int)(r * red);
        int tintedG = (int)(g * green);
        int tintedB = (int)(b * blue);
        int tintedA = (int)(a * alpha);
        return delegate.color(tintedR, tintedG, tintedB, tintedA);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return delegate.texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return delegate.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return delegate.light(240, 240);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return delegate.normal(x, y, z);
    }
}
