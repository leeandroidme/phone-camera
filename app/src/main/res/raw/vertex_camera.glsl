#version 300 es
layout(position=0) vec4 vPosition;
layout(position=1) vec2 inputTextureCoordinate;
varying vec2 textureCoordinate;

void main() {
    gl_Position=vPosition;
    textureCoordinate=inputTextureCoordinate;
}
