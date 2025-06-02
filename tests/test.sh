#!/bin/bash
# Color codes for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color
# Function to check an endpoint
check_endpoint() {
local endpoint=$1
echo -e "\n${BLUE}========================================${NC}"
echo -e "${BLUE}Checking endpoint: $endpoint${NC}"
echo -e "${BLUE}========================================${NC}"
# Make the request and store the response
local RESPONSE=$(curl -s -w "\n%{http_code}" $endpoint)
# Extract the status code from the response
local HTTP_STATUS=$(echo "$RESPONSE" | tail -n1)
local BODY=$(echo "$RESPONSE" | sed '$d')
# Check if the status code is 200
if [ "$HTTP_STATUS" -eq 200 ]; then
echo -e "${GREEN}✓ SUCCESS: Endpoint returned status code 200${NC}"
echo "Response body:"
echo "$BODY" | head -20 # Show first 20 lines of response for brevity
else
echo -e "${RED}✗ ERROR: Endpoint returned status code $HTTP_STATUS${NC}"
echo "Full response:"
echo "$BODY"
# Additional error details
echo -e "\n${RED}Error Details:${NC}"
echo "URL: $endpoint"
echo "Status Code: $HTTP_STATUS"
echo "Time: $(date)"
fi
}
# Endpoints to check
ENDPOINT1="http://localhost:9898/kpi/summary?userId=1"
ENDPOINT2="http://localhost:9898/todolist/usersummary/2"
# Check both endpoints
check_endpoint "$ENDPOINT1"
check_endpoint "$ENDPOINT2"