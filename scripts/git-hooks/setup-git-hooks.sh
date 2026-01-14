#!/bin/sh
# Git hooksPath를 scripts/git-hooks로 설정합니다.

set -e

ROOT_DIR="$(git rev-parse --show-toplevel)"

if [ ! -d "$ROOT_DIR/scripts/git-hooks" ]; then
  echo "scripts/git-hooks 디렉토리가 없습니다." >&2
  exit 1
fi

git config core.hooksPath scripts/git-hooks

echo "git hooksPath를 scripts/git-hooks로 설정했습니다."
