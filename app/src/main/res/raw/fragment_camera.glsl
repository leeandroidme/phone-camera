#version 300 es
#extension GL_OES_EGL_image_txternal:require
precision mediump float;
varying vec2 textureCoordinate;
uniform samplerExternalOES s_texture;
void main() {
    gl_FragColor = texture2D(s_texture, textureCoordinate);
}
