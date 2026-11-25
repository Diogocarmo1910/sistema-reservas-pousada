# Como usar o sistema de reservas da pousada

Guia para abrir o programa, cadastrar, reservar, cobrar e mostrar o trabalho. Cada passo diz o número que você digita no menu.

A especificação pedida pela disciplina, com requisitos, mockups das telas, diagrama de classes em UML e o quadro Kanban para copiar no Trello, está em [ESPECIFICACAO.md](ESPECIFICACAO.md).

Abra o prompt **dentro da pasta do projeto** (`trabalho_java`). Os arquivos da pousada são gravados na pasta `dados`, ao lado do código.

Na faculdade, copie a pasta do projeto ou descompacte o zip. Entre nessa pasta e dê dois cliques em `executar.bat`. O computador do laboratório usa o Java instalado lá. A pasta `dados` nasce nesse computador, com os 3 hóspedes e os 4 quartos de exemplo. O que você cadastrou em casa não vai junto, a menos que você copie a pasta `dados` também.

## 1. Abrir o programa

1. Dê dois cliques em `compilar.bat` e espere a mensagem `Compilado em bin\`.
2. Dê dois cliques em `executar.bat`.

Se o Java já estiver instalado e no PATH, os mesmos comandos no prompt são:

```bat
compilar.bat
executar.bat
```

Na primeira vez, se a pasta `dados` ainda não existir, o sistema avisa que criou um exemplo com 3 hóspedes e 4 quartos. Esse aviso só aparece uma vez.

Para sair, no menu principal digite `0`. A frase `Sistema encerrado. Os dados foram gravados.` confirma que o que você fez foi salvo.

## 2. O que já vem pronto

Hóspedes:

| Nome | CPF | Telefone |
| --- | --- | --- |
| Ana Souza | 529.982.247-25 | 11987654321 |
| Bruno Lima | 390.533.447-05 | 21999887766 |
| Carla Mendes | 111.444.777-35 | 31988776655 |

Quartos:

| Número | Tipo | Pessoas | Diária |
| --- | --- | --- | --- |
| 101 | Simples | 1 | R$ 150,00 |
| 102 | Duplo | 2 | R$ 220,00 |
| 201 | Suíte | 3 | R$ 350,00 |
| 301 | Chalé | 4 | R$ 480,00 |

Para ver isso na tela: menu `1` (Hóspedes) e opção `2` (Listar). Volte com `0`. Menu `2` (Quartos) e opção `2` (Listar). Volte com `0`.

## 3. Como digitar

- Opção de menu: só o número, depois Enter.
- Data: `dd/MM/aaaa`. Exemplo: `24/11/2026`. A entrada da reserva não pode ser anterior a hoje.
- Dinheiro: `150` ou `150,00`.
- CPF: 11 números, com ou sem ponto e traço. `52998224725` e `529.982.247-25` são o mesmo CPF.
- Telefone: 10 ou 11 números.
- Confirmação de exclusão ou cancelamento: `S` ou `N`.

Se a opção, a data ou o valor estiverem errados, o programa pede de novo. Ele não fecha.

## 4. Caminho completo para apresentar

Use a Ana e o quarto 101. A estadia de duas noites custa R$ 300,00 (2 × R$ 150,00). Troque as datas pelos dias reais da apresentação: a entrada tem que ser hoje ou um dia futuro.

1. Menu principal: `3` (Reservas), depois `1` (Incluir).
2. CPF: `52998224725`
3. Quarto: `101`
4. Entrada: amanhã, no formato `dd/MM/aaaa`
5. Saída: dois dias depois da entrada
6. Quantidade de pessoas: `1`
7. A tela mostra a reserva confirmada, com o código `1` se for a primeira reserva.

Para mostrar que o quarto não pode ser reservado duas vezes no mesmo período, repita os mesmos dados. A mensagem é `Quarto indisponível no período informado.`

Pagamento parcial:

1. Volte ao menu principal (`0`) e entre em `4` (Pagamentos), opção `1` (Lançar).
2. Código da reserva: `1`
3. Valor: `100`
4. Forma: `2` (Pix)
5. A situação fica **Parcial** e o saldo em aberto fica **R$ 200,00**.

Check-in:

1. Menu `3` (Reservas), opção `6`.
2. Código: `1`
3. A mensagem diz que o check-in foi feito e o quarto está ocupado. Confira em Quartos, listar: o 101 aparece como Ocupado.

Check-out com atraso:

1. Ainda em Reservas, opção `7`.
2. Código: `1`
3. Data real de saída: dois dias **depois** da data de saída da reserva.
4. A multa é de 2 dias × R$ 150,00 × 1,5 = **R$ 450,00**.
5. O total passa a ser R$ 300,00 + R$ 450,00 = R$ 750,00. Já foram pagos R$ 100,00, então o saldo é **R$ 650,00**.
6. O check-out **não termina**. O quarto continua ocupado até o saldo zerar.

Quite e encerre:

1. Pagamentos, lançar, reserva `1`, valor `650`, Pix (`2`).
2. Volte em Reservas, check-out (`7`), o mesmo código e a **mesma** data de saída atrasada.
3. A mensagem diz que o check-out foi concluído e o quarto voltou a ficar disponível.

Prova de que o arquivo guardou:

1. Saia com `0`.
2. Abra de novo com `executar.bat`.
3. Reservas, listar (`2`). A reserva da Ana continua lá, com status Check-out.

## 5. Menus

### Hóspedes

| Número | O que faz |
| --- | --- |
| 1 | Incluir. Pede nome, CPF, telefone, e-mail e endereço. E-mail e endereço podem ficar vazios: só aperte Enter. |
| 2 | Listar todos. |
| 3 | Buscar por CPF (`1`) ou por nome (`2`). No nome, digite pelo menos 3 letras. |
| 4 | Alterar nome, telefone, e-mail e endereço. O CPF não muda. |
| 5 | Excluir. Pede `S` para confirmar. Não exclui quem tem reserva confirmada ou em check-in. |

### Quartos

| Número | O que faz |
| --- | --- |
| 1 | Incluir. Número, tipo (1 simples, 2 duplo, 3 suíte, 4 chalé), capacidade e diária. |
| 2 | Listar. |
| 3 | Buscar pelo número. |
| 4 | Alterar tipo, capacidade e diária. O número não muda. |
| 5 | Manutenção. Disponível vira manutenção, e manutenção volta a disponível. Quarto ocupado não entra em manutenção. |
| 6 | Excluir, com confirmação. Não exclui quarto com reserva confirmada ou em check-in. |

Quarto em manutenção não aceita reserva nova.

### Reservas

| Número | O que faz |
| --- | --- |
| 1 | Incluir. Uma reserva liga um hóspede a um quarto. |
| 2 | Listar. |
| 3 | Buscar pelo código e mostrar hospedagem, multa, pagamentos e saldo. |
| 4 | Alterar datas, quarto ou quantidade. Só enquanto estiver confirmada e ainda sem pagamento. |
| 5 | Cancelar. Só reserva confirmada. Pede `S`. |
| 6 | Check-in. A reserva precisa estar confirmada e o quarto disponível. |
| 7 | Check-out. Pede a data real de saída. Só conclui com saldo zero. |

A saída tem que ser depois da entrada. O dia da saída fica livre para outra reserva começar nesse mesmo dia.

### Pagamentos

| Número | O que faz |
| --- | --- |
| 1 | Lançar um valor. Formas: 1 dinheiro, 2 Pix, 3 cartão. |
| 2 | Consultar o saldo da reserva. |

O valor tem que ser maior que zero e não pode passar do saldo em aberto. Pagamento já lançado não é apagado.

## 6. Multa e saldo

Saldo em aberto = o que a pessoa deve − o que já pagou.

- Reserva confirmada, em check-in ou em check-out: deve a hospedagem mais a multa.
- Reserva cancelada: deve só a multa. Se já pagou mais do que a multa, a tela mostra **valor a restituir**. O programa só informa o número.

Multa de cancelamento, contando os dias que faltam até a entrada:

- 7 dias ou mais: R$ 0,00
- de 3 a 6 dias: 30% da hospedagem
- menos de 3 dias, inclusive no dia da entrada: 50% da hospedagem

Multa de atraso no check-out: cada dia depois da saída prevista custa a diária da reserva mais 50%. Saída no dia certo ou antes não dá desconto das diárias já contratadas e não cobra atraso.

Situação na consulta:

- **Em aberto:** ainda não pagou nada e existe saldo.
- **Parcial:** pagou uma parte e ainda falta.
- **Quitado:** não há saldo em aberto.

## 7. Onde ficam os dados

Pasta `dados`, criada ao rodar o programa. São arquivos de texto: abra no Bloco de Notas.

- `hospedes.txt`
- `quartos.txt`
- `reservas.txt`
- `pagamentos.txt`
- `sequencia.txt` (próximo código de reserva e de pagamento)

Exemplo do que aparece em `hospedes.txt`:

```text
nome: Ana Souza
cpf: 52998224725
telefone: 11987654321
email: ana@email.com
endereco: Rua das Flores, 10
```

Cada inclusão, alteração, exclusão, pagamento, check-in e check-out grava de novo. Fechar a janela pelo X também pode perder o que ainda não foi gravado: prefira sair pelo `0`.

Para começar do zero, feche o programa, apague a pasta `dados` e abra de novo. O exemplo de 3 hóspedes e 4 quartos volta.

## 8. Se algo der errado

| Mensagem | O que fazer |
| --- | --- |
| `javac não é reconhecido` | Rode pelo `compilar.bat` na pasta do projeto. Ele procura o Java instalado neste computador. |
| `Data de entrada não pode ser anterior a hoje` | Use a data de hoje ou uma data futura. |
| `Data inválida` | Use `dd/MM/aaaa`, com barras. `31/02/2026` não existe. |
| `CPF inválido. Informe 11 dígitos` | Conte os números. Ponto e traço não entram na conta. |
| `Já existe hóspede com este CPF` | Esse CPF já está cadastrado. Busque em Hóspedes, opção 3. |
| `Hóspede não encontrado` | Confira o CPF da tabela da seção 2. |
| `Quarto indisponível no período` | Escolha outro quarto ou outras datas. |
| `Quarto em manutenção não pode ser reservado` | Em Quartos, opção 5, tire o quarto da manutenção. |
| `Check-in exige quarto disponível` | O quarto está ocupado ou em manutenção. |
| `Check-out não concluído` | Lance em Pagamentos exatamente o saldo que a tela mostrou e repita o check-out com a mesma data. |
| `Não há saldo em aberto` | Essa reserva já está quitada. Não lance outro pagamento. |
| `Não é possível excluir hóspede...` | Cancele a reserva ou faça o check-out antes de excluir. |

O programa mostra `Não foi possível concluir:` e volta ao menu. Leia a frase e corrija o dado. Não precisa abrir de novo.
