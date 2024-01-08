@echo off

xcopy "../tsp_tests" "../out/production/java-tsp/tsp_tests" /E /I /Y

cd ../out/production/java-tsp

REM Loop para executar o programa Java 10 vezes
for /l %%i in (1,1,10) do (
    java Advanced ex4.txt 8 2 16 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex5.txt 8 3 25 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex6.txt 8 4 36 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex7.txt 8 5 49 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex8.txt 8 6 64 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex9.txt 8 7 81 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex10.txt 8 8 100 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced sp11.txt 8 9 121 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced uk12.txt 8 10 144 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ex13.txt 8 20 169 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced burma14.txt 8 25 196 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced lau15.txt 8 30 225 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ulysses16.txt 8 35 256 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced gr17.txt 8 40 289 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced ulysses22.txt 8 45 484 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced gr24.txt 8 50 576 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced fri26.txt 8 55 576 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced dantzig42.txt 8 60 1764 0.001 0.5
)

for /l %%i in (1,1,10) do (
    java Advanced att48.txt 8 120 2304 0.001 0.5
)