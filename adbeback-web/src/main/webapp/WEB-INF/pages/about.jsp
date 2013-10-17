<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="about.title"/></title>
</head>
<script type="text/javascript">
var html='<div class="block"><h4>A Propos du jeu</h4>'+
'<div class="block_content">'+
'	<ul style="display: block;" class="tree dynamized">'+
'		<li><a href="#1. Présentation des différentes cases">1. Présentation des différentes cases</a></li>'+
'		<li><a href="#2. Comment jouer ?">2. Comment jouer ?</a></li>'+
'		<li><a href="#3. Comment obtenir mes musiques ?">3. Comment obtenir mes musiques ?</a></li>'+
'		<li><a href="#4. A la fin du jeu">4. A la fin du jeu</a></li>'+
'		<li><a href="#5. Combien puis-je gagner ?">5. Combien puis-je gagner ?</a></li>'+
'		<li><a href="#6. Comment utiliser cet argent ?">6. Comment utiliser cet argent ?</a></li>'+
'	</ul>'+
'</div></div>';
$("#left_column").append(html);
</script>

<body>
<h3>Règle du jeu MTzik :</h3>
<br>
<br>
Le but du jeu est d'avancer de case en case jusqu'à tomber sur la case fin <span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/win.png"/>'></span>.
<br>
<br>
Ce jeu se divise en une multitude de niveaux. Chaque niveau a sa propre cagnotte. Au départ le joueur est sur la case « départ » représenté par <span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/start.png"/>'></span>.
<br>
<br>
Pour avancer, le système se sert du score effectué moins le score minimum pour télécharger. Par exemple, si le score minimum pour télécharger est de 3 et que vous obtenez un score de 6, vous avancez de 3 cases.
<br>
<br>
<a name="1. Présentation des différentes cases">1. Présentation des différentes cases</a>

<br>
<br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/start.png"/>'></span> : Départ du niveau<br><br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/win.png"/>'></span> : Fin du niveau, vous remportez la cagnotte et celle-ci repasse à 0 &euro;<br><br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/addCagnotte.png"/>'></span> : Fait augmenter la valeur de la cagnotte<br><br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/dead.png"/>'></span> : Vous sortez du jeu, pour revenir au début du niveau<br><br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/jail.png"/>'></span> : Vous êtes en prison, pour sortir vous devez faire un score de 6 +  score minimum pour télécharger<br><br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/jump.png"/>'></span> : Vous faite un bond en avant ou en arrière de plusieurs cases<br><br>
<span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/reduc.png"/>'></span> : Vous gagnez une offre promotionnelle chez l'un de nos partenaires, sous forme d'un bon d'achat ou d'une remise<br><br>
<br>
<br>
Votre pion est représenté par <span style="position: relative;"><img style="margin-top:-7px;" width="24px" height="24px" alt="start" src='<c:url value="/images/token.png"/>'></span>.
<br>
<br>
<a name="2. Comment jouer ?">2. Comment jouer ?</a>
<br>
<br>
Une fois que vous avez choisi les musiques que vous voulez télécharger, l'écran suivant s'affiche :
<br>
<br>
<div style="position: relative; text-align: center;"><img alt="start" src='<c:url value="/images/startgame.png"/>'></div>
<br>
<br>
Cliquez sur « Jouez », et le jeu commence.
Ensuite il faut répondre aux questions posées durant les publicités en un temps limité. De plus, vous ne pouvez avoir que 6 erreurs maximum.
<br>
<br>
Exemples :
<br>
<br>
<div style="position: relative; text-align: center;"><img alt="start" src='<c:url value="/images/q1.png"/>'></div>
<br>
<br>
<div style="position: relative; text-align: center;"><img alt="start" src='<c:url value="/images/q2.png"/>'></div>
<br>
<br>
<a name="3. Comment obtenir mes musiques ?">3. Comment obtenir mes musiques ?</a>
<br>
<br>
Le système vous calcule un nombre minimum de points pour pouvoir télécharger. Quand vous répondez correctement à une question vous marquez 1 point. Si vous ne répondez pas ou si vous vous trompez vous ne marquez aucun point.
<br><br>
Dès que vous obtenez le nombre de point suffisant, vous pouvez continuez pour jouer au jeu MTzik et essayer de remporter la cagnotte, soit en cliquant sur le boutton télécharger qui apparâit durant le jeu.
<br>
<br>
<div style="position: relative; text-align: center;"><img alt="start" src='<c:url value="/images/directdl.png"/>'></div>
<br>
<br>
<a name="4. A la fin du jeu">4. A la fin du jeu</a>
<br>
<br>
Si vous obtenez le nombre de points suffisants vous verrez l'écran suivant :
<br>
<br>
<div style="position: relative; text-align: center;"><img alt="start" src='<c:url value="/images/endgame.png"/>'></div>
<br>
<br>
Cet écran affiche le déplacement de votre pion que vous avez effectué. Et vous permet d'obtenir vos musiques en cliquant sur « Cliquez pour obtenir vos musiques ». Le fichier étant crypté vous devrez attendre quelques secondes avant de voir la fenêtre vous demandant l'emplacement d'enregistrement du fichier.
<br>
<br>
<div style="position: relative; text-align: center;"><img alt="start" src='<c:url value="/images/save.png"/>'></div>
<br>
<br>
<a name="5. Combien puis-je gagner ?">5. Combien puis-je gagner ?</a>
<br>
<br>
La somme dépend du nombre d'internautes qui participent au jeu, et quand vous tomber sur la case de fin du niveau.
<br>
<br>
Prenons un exemple :
<br>
- il y a 10000 joueurs
<br>
- pour gagner il faut jouer au minimum 11 fois
<br>
<br>
Avec cet exemple, la cagnotte pour le premier qui arrivera sur la case de fin du niveau obtiendra 1000*0.15*11 = <b>16500 &euro;</b>.
<br>
<br>
<a name="6. Comment utiliser cet argent ?">6. Comment utiliser cet argent ?</a>
<br>
<br>
L'argent sera transféré sur votre compte PayAndReceive. Vous pourrez alors l'utiliser chez tous les commerçants utilisant ce moyen de paiement.
<br>
<br>
Si vous n'avez pas de compte, vous devrez en créer un pour pouvoir toucher la cagnotte.
</body>
