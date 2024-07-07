#!/bin/bash
##
## kopiert den Inhalt des builds dorthin, wo quarkus ihn gern hätte
##

SOURCE_DIR="dist/bv-admin-app/browser"
TARGET_DIR="../../backend/auth-admin-api/src/main/resources/META-INF/resources/bv-admin-app"

if [ -d "$TARGET_DIR" ]; then
  echo "Removing existing target directory: $TARGET_DIR"
  rm -rf "$TARGET_DIR"
fi

echo "Creating target directory: $TARGET_DIR"
mkdir -p "$TARGET_DIR"

echo "Moving build output from $SOURCE_DIR to $TARGET_DIR"
mv "$SOURCE_DIR"/* "$TARGET_DIR"

echo "built angular app successfully moved"
