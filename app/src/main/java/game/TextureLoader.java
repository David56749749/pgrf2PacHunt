package game;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class TextureLoader {
    private static final Map<String, Integer> cache = new HashMap<>();

    /**
     * Loads a texture from the given resource path, caching the result.
     */
    public static int loadTexture(String resourcePath) {
        if (cache.containsKey(resourcePath)) {
            return cache.get(resourcePath);
        }
        int textureID = doLoad(resourcePath);
        cache.put(resourcePath, textureID);
        return textureID;
    }

    /**
     * Frees all cached textures. Call on shutdown.
     */
    public static void cleanup() {
        for (int texID : cache.values()) {
            glDeleteTextures(texID);
        }
        cache.clear();
    }

    private static int doLoad(String resourcePath) {
        try (InputStream stream = TextureLoader.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new RuntimeException("Resource not found on classpath: " + resourcePath);
            }
            byte[] data = stream.readAllBytes();
            ByteBuffer imageBuffer = MemoryUtil.memAlloc(data.length);
            imageBuffer.put(data).flip();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                STBImage.stbi_set_flip_vertically_on_load(true);
                ByteBuffer decodedImage = STBImage.stbi_load_from_memory(imageBuffer, width, height, channels, 4);
                if (decodedImage == null) {
                    throw new RuntimeException("Failed to decode texture: " + STBImage.stbi_failure_reason());
                }

                int textureID = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, textureID);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                        width.get(0), height.get(0), 0,
                        GL_RGBA, GL_UNSIGNED_BYTE, decodedImage);
                glGenerateMipmap(GL_TEXTURE_2D);

                STBImage.stbi_image_free(decodedImage);
                MemoryUtil.memFree(imageBuffer);
                return textureID;
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load texture resource: " + resourcePath, e);
        }
    }
}
