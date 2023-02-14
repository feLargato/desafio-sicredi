
## Exemplo de uso

1. Crie uma pauta:
| Método |   URL   |
|--------|----------|
| POST   | http://localhost:8081/pautas  |  

- Request body
```
{
    "pauta": "descricao da pauta"
}
```
- Response body
```
{
  "id": 1,
  "pauta": "descricao da pauta"
}
```
2. Listar todas as pautas:
| Método |   URL   |
|--------|----------|
| GET    | http://localhost:8081/pautas  |  
- Response body: 
```
[
    {
        "id": 1,
        "pauta": "descricao da pauta"
    }
]
```
3. Abrir votação:
| Método |   URL   |
|--------|----------|
| POST   | http://localhost:8082/votacoes| 
- Request body
```
{
  "duracao": 1,
  "pautaId": 1
}
``` 
- Response body
```
{
    "id": "63ebc970de94264b9df919bb",
    "pautaId": 1,
    "statusVotacao": "ABERTO",
    "duracao": 1,
    "iniciaEm": "2023-02-14T14:48:32.3779837",
    "terminaEm": "2023-02-14T14:58:32.3779837"
}
```
4. Listar todas as votações:
| Método |   URL   |
|--------|----------|
| GET    | http://localhost:8082/votacoes| 
- Response body 
```
[
    {
        "id": "63e44598f0af705068008625",
        "pautaId": 1,
        "statusVotacao": "ABERTO",
        "duracao": 1,
        "iniciaEm": "2023-02-14T14:48:32.3779837",
        "terminaEm": "2023-02-14T14:58:32.3779837"
    }
]
```
4. Votar: 
| Método |   URL   |
|--------|----------|
| POST   | http://localhost:8082/votos/votar   |
- Request body 
```
{
  "cpf": "11122233344",
  "opcaoDeVoto": "SIM",
  "pautaId": 1
}
``` 
- Response body 
```
{
    "votoId": "63ebcb37de94264b9df919bc",
    "pautaId": 1,
    "cpf": "11122233344",
    "opcaoDeVoto": "SIM"
}
```
5. Consultar o resultado da votação
| Método |   URL   |
|--------|----------|
| GET   | http://localhots:8081/pautas/resultado/{pautaId}   |
- Response body
```
{
  "pautaId": 1,
  "votosSim": 0,
  "votosNao": 1,
  "votosContabilizados": 1,
  "resultado": "A pauta foi reprovada"
}
```
