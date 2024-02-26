# Projeto Processor

## Objetivo do Projeto

A aplicação tem como intuito receber e manejar pagamentos recebidos, com algumas regras.

### Requisitos para o projeto

#### 1 - A API deve ser capaz de receber um pagamento

Cada pagamento tem as seguintes informações

- Código do Débito sendo pago
- CPF ou CNPJ do pagador
- Método de pagamento (boleto, pix, cartão de crédito ou cartão de débito)
- Número do cartão (Necessário somente caso o método de pagamento seja um cartão)
- Valor do pagamento

#### 2 - A API deve ser capaz de atualizar o status de um pagamento

- A atualização de status de um pagamento sempre irá conter o ID do Pagamento e o Novo Status
- Quando o pagamento está pendente de Processamento, ele pode ser alterado para Processado com Sucesso ou Processado com
  Falha
- Quando o pagamento está Processado com Sucesso, ele não pode ter seu status alterado
- Quando o pagamento está Processado com Falha, ele só pode ter seu status alterado para Pendente de Processamento

#### 3 - A API deve ser capaz de listar todos os pagamentos recebidos e oferecer filtros de busca para o cliente.

- Possiveis filtros:
    - Código do débito
    - CPF ou CNPJ
    - Status do Pagamento

#### 4 - A API deve ser capaz de deletar um pagamento, desde que este ainda esteja com status Pendente de Processamento.

#### 5 - Rotas da aplicação

    GET
    -> /payment - listagem da pagamentos registrados
        FILTROS - Passado por RequestParams
        - codigoDebito - Código informado no cadastro do débito
        - cpfCnpj - CPF ou CNPJ informado no cadastro do débito
        - statusPagamento - Status atual do pagamento
    
    -> /payment/paginated - listagem da pagamentos registrados, com paginação        
       FILTROS - Passado por RequestParams
        - codigoDebito - Código informado no cadastro do débito
        - cpfCnpj - CPF ou CNPJ informado no cadastro do débito
        - statusPagamento - Status atual do pagamento
        - page - Pagina da consulta (Se não informado trás a pagina 1)
        - size - Tamanho de cada página (Se não informado utiliza tamanho 10)
    
    POST
    -> /payment - Criar novo pagamento
        - POSSIVEIS MÉTODOS DE PAGAMENTO:
            BOLETO
            PIX
            CARTAO_CREDITO
            CARTAO_DEBITO
        - Request Body Ex: {
        "codigoDebito": 12,
        "cpfCnpj": "02127107292",
        "metodoPagamento": "CARTAO_CREDITO",
        "numeroCartao": "1092412094",
        "valorPagamento":500.22
        }
    PUT
    -> /payment/{idPagamento}
      - POSSIVEIS STATUS:
          PENDENTE_PROCESSAMENTO
          PROCESSADO_COM_SUCESSO
          PROCESSADO_COM_FALHA
      - Request Body Ex: {
          "statusPagamento": PENDENTE_PROCESSAMENTO
      }
    DELETE
    -> /payment/{codigoDebito}
