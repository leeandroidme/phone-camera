#version 300 es
layout(location = 0) in vec4 vPosition;
layout(location = 1) in vec2 inputTextureCoordinate;
out vec2 textureCoordinate;
void main() {
    gl_Position=vPosition;
    textureCoordinate=inputTextureCoordinate;
}
