#!/bin/bash
# Salva o estado atual das portas
nmap localhost -p 1-10000 > estado_atual.txt

# Compara com um estado seguro anterior
diff estado_atual.txt estado_seguro.txt > alerta.txt

if [ -s alerta.txt ]; then
  echo "ALERTA: Nova porta aberta no DebtTracker!"
fi