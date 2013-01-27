#import('dart:html');
#source('Token.dart');

void main() {
  showMessage('Welcome to Dart!');
}

void showMessage(String message) {
  var textElement = query('#text');
  Token t = new Token();

  textElement.text = message;
}
