@echo off
echo ========================================
echo Iniciando MySQL con Docker
echo ========================================
echo.

docker-compose up -d

echo.
echo Esperando a que MySQL este listo...
timeout /t 5 /nobreak >nul

echo.
echo Verificando estado del contenedor...
docker-compose ps

echo.
echo ========================================
echo MySQL esta corriendo!
echo.
echo Para ver los logs: docker-compose logs -f mysql
echo Para detener: docker-compose down
echo ========================================
pause

