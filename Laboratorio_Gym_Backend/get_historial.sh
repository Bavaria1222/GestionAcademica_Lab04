#!/bin/bash
# Script to fetch matricula history for sample students S001 and S002
# It calls the API endpoints provided by the backend server

API_BASE="http://localhost:8080/api/matriculas/alumno"
for cedula in S001 S002; do
  echo "Fetching historial for student $cedula"
  curl -s "${API_BASE}/$cedula"
  echo -e "\n"
done
