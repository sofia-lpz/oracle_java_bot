#!/bin/bash

# Script to run ZAP security scan against localhost from Docker and output XML report

# Set variables
TARGET_URL="http://localhost:9898"
REPORT_NAME="zap_scan_report"
CONTAINER_NAME="zap_security_scan"
REPORT_DIR="./zap_reports"

# Create reports directory if it doesn't exist
mkdir -p "$REPORT_DIR"

# Make sure the directory has the right permissions for Docker container to write to
chmod 777 "$REPORT_DIR"

echo "Starting ZAP security scan against $TARGET_URL..."

# Run ZAP scan from Docker using the latest stable image
# Using host network mode to access localhost on the host machine
docker run --name "$CONTAINER_NAME" --rm \
  --network="host" \
  -v "$(pwd)/$REPORT_DIR:/zap/wrk:rw" \
  ghcr.io/zaproxy/zaproxy:stable \
  zap-baseline.py \
  -t "$TARGET_URL" \
  -x "$REPORT_NAME.xml" \
  -d

# Check if scan was successful
if [ $? -eq 0 ]; then
  echo "ZAP scan completed successfully."
  
  # Print the XML report
  if [ -f "$REPORT_DIR/$REPORT_NAME.xml" ]; then
    echo "XML Report contents:"
    echo "===================="
    cat "$REPORT_DIR/$REPORT_NAME.xml"
    echo "===================="
    echo "Report saved to $REPORT_DIR/$REPORT_NAME.xml"
  else
    echo "Error: XML report file not found."
  fi
else
  echo "Error: ZAP scan failed."
  echo "Check if there are permission issues with the report directory."
fi
