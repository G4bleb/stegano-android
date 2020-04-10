# 8INF865 Plateforrmes mobiles
Il est demandé de concevoir et d’implémenter une galerie de photos qui permet de cacher du texte dans une image.
Pour cela, vous devez
1. Afficher toutes les photos de l’appareil dans une sorte de galerie d’images.
2. Un double clique sur une photo doit permettre d’afficher la photo en grand à l’écran.
3. Lorsqu’une photo est affichée en grand, il faut aussi avoir une zone de texte et un bouton.
4. Lorsque l’on clique sur le bouton alors le texte saisi sera sauvegardé dans la photo en remplaçant les deux bits de poids les plus faibles de l’image par les octets du texte. Pensez à mettre un timer qui montre le temps requis pour le cryptage de l’information
5. Lorsque l’on affiche une image en grand s’il y avait un texte qui était sauvegardé dans l’image alors il doit être affiché dans la zone de texte.

Le lien suivant donne des explications la technique que je vous invite à
implémenter.
https://fr.wikipedia.org/wiki/St%C3%A9ganographie

Pensez à utiliser les opération logiques pour la rapidité du code.