@echo off

xcopy "../tsp_tests" "../out/production/java-tsp/tsp_tests" /E /I /Y

cd ../out/production/java-tsp

REM Loop para executar o programa Java 10 vezes
for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex4.txt 8 3 100 0.009
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex5.txt 8 3 100 0.9
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex6.txt 8 3 100 0.9
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex7.txt 8 3 100 0.99
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex8.txt 8 3 100 0.99
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex9.txt 8 3 1000 0.99
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex10.txt 8 3 1000 0.99
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP sp11.txt 8 5 1000 0.999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP uk12.txt 8 5 1000 0.999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ex13.txt 8 5 1000 0.999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP burma14.txt 8 5 1000 0.999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP lau15.txt 8 5 1000 0.999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ulysses16.txt 8 5 100000 0.9999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP gr17.txt 8 5 100000 0.9999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP ulysses22.txt 8 10 1000000 0.99999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP gr24.txt 8 10 1000000 0.99999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP fri26.txt 8 10 1000000 0.99999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP dantzig42.txt 8 60 1000000 0.999999
    echo.
)

for /l %%i in (1,1,10) do (
    java SimulatedAnnealingTSP att48.txt 8 120 100000 0.9999999
    echo.
)

echo Testes terminados!
pause