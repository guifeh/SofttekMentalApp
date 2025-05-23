🧠 Softek Mental App
Aplicativo Android nativo desenvolvido com Kotlin e Jetpack Compose, focado em promover o bem-estar emocional dos usuários, através do registro de check-ins emocionais, autoavaliações psicossociais e geração de insights sobre a saúde mental ao longo do tempo.

📲 Funcionalidades
✅ Realização de check-ins emocionais diários.
✅ Registro e análise de avaliações psicossociais.
✅ Visualização de histórico completo de check-ins e avaliações.
✅ Filtros por data e opção de limpar histórico.
✅ Tela de insights para acompanhamento da evolução emocional.

🛠️ Tecnologias Utilizadas
Tecnologia	Descrição
Kotlin	Linguagem oficial para desenvolvimento Android.
Jetpack Compose	Framework declarativo para construção de interfaces.
Room (ou SQLite)	Persistência local de dados.
Navigation Compose	Navegação entre telas de forma segura e eficiente.
Kotlin Coroutines	Manipulação de operações assíncronas e não bloqueantes.

🏛️ Arquitetura
O projeto segue uma arquitetura MVP simplificada:

Model:
Representação das entidades Checkin e AssessmentResult, além dos repositórios responsáveis pela persistência local.

View:
Telas construídas com Jetpack Compose, reativas e modulares, focadas na interação com o usuário.

Presenter:
Lógica intermediária implementada por meio de repositórios e ViewModels simplificados utilizando estados reativos (mutableStateOf).

🧭 Fluxo da Aplicação
Check-in:
→ Usuário escolhe uma emoção.
→ Emoção é registrada no banco local.
→ Redirecionamento para tela de feedback.

Histórico:
→ Listagem de check-ins e avaliações.
→ Filtros por data e opção para limpar todo o histórico.

Insights:
→ Visualização de relatórios sobre evolução emocional e resultados das autoavaliações.

🎯 Como Executar o Projeto
✅ Pré-requisitos
Android Studio Giraffe ou superior.

Gradle atualizado.

SDK Android versão 33+.

✅ Passos
Clone o repositório:
bash
Copiar
Editar
git clone https://github.com/seu-usuario/softek-mental-app.git
Abra no Android Studio.

Execute o projeto em um emulador ou dispositivo Android.

💡 Funcionalidades Futuras
🚀 Integração com Firebase para backup e autenticação.
🚀 Notificações push para lembretes de check-ins.
🚀 Gráficos interativos com bibliotecas como ChartsCompose.
🚀 Internacionalização para outros idiomas.

✅ Contribuindo
Contribuições são bem-vindas!

Faça um fork do projeto.

Crie uma branch com a sua feature: git checkout -b minha-feature.

Commit suas alterações: git commit -m 'Minha nova feature'.

Push para a branch: git push origin minha-feature.

Abra um Pull Request.

📄 Licença
Este projeto está licenciado sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

🙋‍♂️ Autor
Desenvolvido por: Guilherme


⭐️ Agradecimentos
FIAP – Faculdade de Informática e Administração Paulista

Professores e colegas que contribuíram com feedbacks valiosos.
