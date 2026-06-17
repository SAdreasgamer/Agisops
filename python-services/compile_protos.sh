#!/bin/bash
set -e

# Change directory to script location
cd "$(dirname "$0")"

echo "Creating directories..."
mkdir -p gen/aegisops
touch gen/__init__.py
touch gen/aegisops/__init__.py

echo "Compiling Protobuf files..."
./venv/bin/python -m grpc_tools.protoc -I../proto --python_out=gen --grpc_python_out=gen ../proto/aegisops/*.proto

# Fix relative imports in generated python files for Python 3 compatibility
echo "Patching imports..."
# find gen/aegisops -name "*_pb2*.py" -exec sed -i '' -E 's/import ([a-zA-Z0-9_]+_pb2)/from aegisops import \1/g' {} + || true

echo "Protobuf compilation completed successfully!"
