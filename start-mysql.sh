#!/bin/bash

echo "========================================"
echo "Iniciando MySQL con Docker"
echo "========================================"
echo ""

docker-compose up -d

echo ""
echo "Esperando a que MySQL esté listo..."
sleep 5

echo ""
echo "Verificando estado del contenedor..."
docker-compose ps

echo ""
echo "========================================"
echo "MySQL está corriendo!"
echo ""
echo "Para ver los logs: docker-compose logs -f mysql"
echo "Para detener: docker-compose down"
echo "========================================"

